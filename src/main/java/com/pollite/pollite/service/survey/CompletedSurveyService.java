package com.pollite.pollite.service.survey;

import com.pollite.pollite.dto.*;
import com.pollite.pollite.dto.mapper.CompletedSurveyMapper;
import com.pollite.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.exception.SurveyNotActiveException;
import com.pollite.pollite.model.survey.CompletedSurvey;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.pollite.model.survey.question.CompletedQuestion;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import com.pollite.pollite.projection.SurveyResultsProjection;
import com.pollite.pollite.repository.CompletedSurveyRepository;
import com.pollite.pollite.repository.SurveyRepository;
import com.pollite.pollite.validator.CompletedSurveyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Predicates;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

@Service
@RequiredArgsConstructor
public class CompletedSurveyService {

    private final CompletedSurveyRepository completedSurveyRepository;
    private final SurveyRepository surveyRepository;
    private final CompletedSurveyMapper completedSurveyMapper;
    private final CompletedSurveyValidator completedSurveyValidator;

    public void submitCompletedSurvey(CompletedSurveyDto completedSurveyDto) {
        completedSurveyValidator.validate(completedSurveyDto);

        var completedSurvey = completedSurveyMapper.fromDto(completedSurveyDto);

        completedSurveyRepository.save(completedSurvey);
    }

    @Transactional
    public SurveyResultsDto getSurveyResults(Long surveyId, List<CompletedSurveyFilter> filters) {
        var survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyDoesNotExistException(surveyId));

        var completedSurveys = completedSurveyRepository.findAllBySurveyId(surveyId)
                .filter(filtersToPredicate(filters))
                .toList();

        return calculateResults(survey, completedSurveys);
    }

    private SurveyResultsDto calculateResults(Survey survey, List<CompletedSurvey> completedSurveys) {
        Map<Long, Map<Long, Integer>> results = new HashMap<>();

        for (SurveyQuestion question: survey.getQuestions()) {
            results.put(question.getId(), new HashMap<>());
            var answersMap = results.get(question.getId());

            for (SurveyQuestionAnswer answer: question.getAnswers()) {
                answersMap.put(answer.getId(), 0);
            }
        }

        for (CompletedSurvey completedSurvey: completedSurveys) {
            for (CompletedQuestion completedQuestion: completedSurvey.getCompletedQuestions()) {
                var questionAnswersMap = results.get(completedQuestion.getQuestion().getId());

                for (SurveyQuestionAnswer surveyQuestionAnswer: completedQuestion.getAnswers()) {
                    questionAnswersMap.merge(surveyQuestionAnswer.getId(), 1, Integer::sum);
                }
            }
        }

        return mapToSurveyResultsDto(survey, results);
    }

    private SurveyResultsDto mapToSurveyResultsDto(Survey survey, Map<Long, Map<Long, Integer>> resultsMap) {
        var questionsResults = resultsMap.entrySet().stream()
                .map(entry ->
                        mapToQuestionResultsDto(
                                entry.getKey(),
                                entry.getValue(),
                                survey.getQuestionById(entry.getKey())
                        )
                ).toList();

        return SurveyResultsDto.builder().questionsResults(questionsResults).build();
    }

    private SurveyQuestionResultsDto mapToQuestionResultsDto(Long questionId,
                                                             Map<Long, Integer> answersMap,
                                                             SurveyQuestion question) {
        var answersResults = answersMap.entrySet().stream()
                .map(entry ->
                        mapToAnswerResultsDto(
                                entry.getKey(),
                                question.getAnswerById(entry.getKey()).getText(), entry.getValue()
                        )
                ).toList();

        return SurveyQuestionResultsDto.builder()
                .questionId(questionId)
                .questionText(question.getText())
                .answersResults(answersResults)
                .build();
    }

    private SurveyQuestionAnswerResultsDto mapToAnswerResultsDto(Long answerId, String answerText, int total) {
        return SurveyQuestionAnswerResultsDto.builder()
                .answerId(answerId)
                .answerText(answerText)
                .total(total)
                .build();
    }



    private Predicate<CompletedSurvey> filtersToPredicate(List<CompletedSurveyFilter> filters) {
        if (filters == null) {
            return p -> true;
        }

        return filters.stream()
                .map(CompletedSurveyFilter::toPredicate)
                .reduce(p -> true, Predicate::and);
    }

//    public SurveyResultsDto getSurveyResults(Long surveyId) throws SurveyDoesNotExistException {
//        var survey = surveyRepository.findById(surveyId)
//                .orElseThrow(() -> new SurveyDoesNotExistException(surveyId));
//
//        var resultsProjection = completedSurveyRepository.countAnswers(surveyId);
//
//        Map<Long , Map<Long, List<SurveyResultsProjection>>> results = resultsProjection.stream()
//                .collect(
//                        groupingBy(
//                                SurveyResultsProjection::getQuestionId,
//                                groupingBy(SurveyResultsProjection::getAnswerId)));
//
//        return toSurveyResultsDto(survey, results);
//    }
//
//    private SurveyResultsDto toSurveyResultsDto(Survey survey,
//                                                Map<Long , Map<Long, List<SurveyResultsProjection>>> results) {
//        var questionResults = results.entrySet()
//                .stream()
//                .map(entry -> toSurveyQuestionResultsDto(survey, entry.getKey(), entry.getValue()))
//                .toList();
//
//        return SurveyResultsDto.builder()
//                .questionsResults(questionResults)
//                .build();
//    }
//
//    private SurveyQuestionResultsDto toSurveyQuestionResultsDto(Survey survey,
//                                                                Long questionId,
//                                                                Map<Long, List<SurveyResultsProjection>> resultsByAnswerId) {
//        var answersResults = resultsByAnswerId.values().stream()
//                .map(surveyResultsProjections -> toQuestionAnswerResultsDto(
//                        survey.getQuestionById(questionId),
//                        surveyResultsProjections.get(0))
//                )
//                .toList();
//
//        return SurveyQuestionResultsDto.builder()
//                .questionId(questionId)
//                .questionText(survey.getQuestionById(questionId).getText())
//                .answersResults(answersResults)
//                .build();
//    }
//
//    private SurveyQuestionAnswerResultsDto toQuestionAnswerResultsDto(SurveyQuestion question,
//                                                                      SurveyResultsProjection surveyResultsProjection) {
//        return SurveyQuestionAnswerResultsDto.builder()
//                .answerId(surveyResultsProjection.getAnswerId())
//                .total(surveyResultsProjection.getTotal())
//                .answerText(question.getAnswerById(surveyResultsProjection.getAnswerId()).getText())
//                .build();
//    }

}
