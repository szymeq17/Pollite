package com.pollite.pollite.dto.mapper;

import com.pollite.pollite.dto.CompletedSurveyDto;
import com.pollite.pollite.dto.CompletedSurveyQuestionDto;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.exception.SurveyQuestionDoesNotExistException;
import com.pollite.pollite.model.survey.CompletedSurvey;
import com.pollite.pollite.model.survey.question.CompletedQuestion;
import com.pollite.pollite.repository.SurveyQuestionAnswerRepository;
import com.pollite.pollite.repository.SurveyQuestionRepository;
import com.pollite.pollite.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompletedSurveyMapper {

    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyQuestionAnswerRepository surveyQuestionAnswerRepository;

    public CompletedSurvey fromDto(CompletedSurveyDto completedSurveyDto) throws SurveyDoesNotExistException {
        var survey = surveyRepository.findById(completedSurveyDto.getSurveyId())
                .orElseThrow(() -> new SurveyDoesNotExistException(completedSurveyDto.getSurveyId()));

        var completedQuestionsDtos = completedSurveyDto.getCompletedQuestions();

        var completedQuestions = completedQuestionsDtos.stream()
                .map(this::mapDtoToCompletedQuestion)
                .toList();

        return CompletedSurvey.builder()
                .survey(survey)
                .completedQuestions(completedQuestions)
                .build();
    }

    private CompletedQuestion mapDtoToCompletedQuestion(CompletedSurveyQuestionDto completedSurveyQuestionDto) {
        var question = surveyQuestionRepository
                .findById(completedSurveyQuestionDto.getQuestionId())
                .orElse(null);
        var answers = surveyQuestionAnswerRepository
                .findAllById(completedSurveyQuestionDto.getQuestionAnswerIds());

        return CompletedQuestion.builder().question(question).answers(answers).build();
    }
}
