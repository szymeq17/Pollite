package com.pollite.pollite.service.survey;

import com.pollite.pollite.dto.*;
import com.pollite.pollite.dto.mapper.CompletedSurveyMapper;
import com.pollite.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.exception.SurveyNotActiveException;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import com.pollite.pollite.projection.SurveyResultsProjection;
import com.pollite.pollite.repository.CompletedSurveyRepository;
import com.pollite.pollite.repository.SurveyRepository;
import com.pollite.pollite.validator.CompletedSurveyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class CompletedSurveyService {

    private final CompletedSurveyRepository completedSurveyRepository;
    private final SurveyRepository surveyRepository;
    private final CompletedSurveyMapper completedSurveyMapper;
    private final CompletedSurveyValidator completedSurveyValidator;

    public void submitCompletedSurvey(CompletedSurveyDto completedSurveyDto)
            throws SurveyDoesNotExistException, InvalidCompletedSurveyException, SurveyNotActiveException {
        completedSurveyValidator.validate(completedSurveyDto);

        var completedSurvey = completedSurveyMapper.fromDto(completedSurveyDto);

        completedSurveyRepository.save(completedSurvey);
    }

    public SurveyResultsDto getSurveyResults(Long surveyId) throws SurveyDoesNotExistException {
        var survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyDoesNotExistException(surveyId));

        var resultsProjection = completedSurveyRepository.countAnswers(surveyId);

        Map<Long , Map<Long, List<SurveyResultsProjection>>> results = resultsProjection.stream()
                .collect(
                        groupingBy(
                                SurveyResultsProjection::getQuestionId,
                                groupingBy(SurveyResultsProjection::getAnswerId)));

        return toSurveyResultsDto(survey, results);
    }

    private SurveyResultsDto toSurveyResultsDto(Survey survey,
                                                Map<Long , Map<Long, List<SurveyResultsProjection>>> results) {
        var questionResults = results.entrySet()
                .stream()
                .map(entry -> toSurveyQuestionResultsDto(survey, entry.getKey(), entry.getValue()))
                .toList();

        return SurveyResultsDto.builder()
                .questionsResults(questionResults)
                .build();
    }

    private SurveyQuestionResultsDto toSurveyQuestionResultsDto(Survey survey,
                                                                Long questionId,
                                                                Map<Long, List<SurveyResultsProjection>> resultsByAnswerId) {
        var answersResults = resultsByAnswerId.values().stream()
                .map(surveyResultsProjections -> toQuestionAnswerResultsDto(
                        survey.getQuestionById(questionId),
                        surveyResultsProjections.get(0))
                )
                .toList();

        return SurveyQuestionResultsDto.builder()
                .questionId(questionId)
                .questionText(survey.getQuestionById(questionId).getText())
                .answersResults(answersResults)
                .build();
    }

    private SurveyQuestionAnswerResultsDto toQuestionAnswerResultsDto(SurveyQuestion question,
                                                                      SurveyResultsProjection surveyResultsProjection) {
        return SurveyQuestionAnswerResultsDto.builder()
                .answerId(surveyResultsProjection.getAnswerId())
                .total(surveyResultsProjection.getTotal())
                .answerText(question.getAnswerById(surveyResultsProjection.getAnswerId()).getText())
                .build();
    }

}
