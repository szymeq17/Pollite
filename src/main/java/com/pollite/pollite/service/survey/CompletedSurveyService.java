package com.pollite.pollite.service.survey;

import com.pollite.pollite.dto.*;
import com.pollite.pollite.dto.mapper.CompletedSurveyMapper;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.model.survey.CompletedSurvey;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.pollite.model.survey.question.CompletedQuestion;
import com.pollite.pollite.model.survey.question.SurveyQuestion;
import com.pollite.pollite.repository.CompletedSurveyRepository;
import com.pollite.pollite.repository.SurveyRepository;
import com.pollite.pollite.validator.CompletedSurveyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        var completedSurveys = completedSurveyRepository.findAllBySurveyId(surveyId).toList();
        var completedSurveysFiltered = completedSurveyRepository.findAllBySurveyId(surveyId)
                .filter(filtersToPredicate(filters))
                .toList();

        var results = calculateResults(survey, completedSurveys);
        var resultsFiltered = calculateResults(survey, completedSurveysFiltered);

        return calculateTotalsAndPercentages(results, resultsFiltered);
    }

    private SurveyResultsDto calculateTotalsAndPercentages(SurveyResultsDto results, SurveyResultsDto resultsFiltered) {
        return SurveyResultsDto.builder().questionsResults(resultsFiltered.getQuestionsResults().stream()
                .map(questionResults ->
                        SurveyQuestionResultsDto.builder()
                                .total(questionResults.getTotal())
                                .questionText(questionResults.getQuestionText())
                                .questionId(questionResults.getQuestionId())
                                .answersResults(
                                        questionResults.getAnswersResults().stream()
                                                .map(answerResults ->
                                                        SurveyQuestionAnswerResultsDto.builder()
                                                                .total(answerResults.getTotal())
                                                                .percentage(calculatePercentage(
                                                                        answerResults.getTotal(),
                                                                        results.findQuestionResultsById(questionResults.getQuestionId())
                                                                                .getTotal()
                                                                ))
                                                                .answerText(answerResults.getAnswerText())
                                                                .answerId(answerResults.getAnswerId())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build()
                )
                .collect(Collectors.toList()))
                .build();
    }

    private BigDecimal calculatePercentage(int first, int second) {
        if (second == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(first)
                .divide(
                        BigDecimal.valueOf(second),
                        new MathContext(4, RoundingMode.HALF_DOWN)
                )
                .scaleByPowerOfTen(2);
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
                .total(answersMap.values().stream().reduce(Integer::sum).orElse(0))
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
}
