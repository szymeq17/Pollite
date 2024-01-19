package com.pollite.validator;

import com.pollite.dto.CompletedSurveyDto;
import com.pollite.dto.CompletedSurveyQuestionDto;
import com.pollite.model.survey.question.SurveyQuestion;
import com.pollite.model.survey.question.SurveyQuestionType;
import com.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.exception.SurveyDoesNotExistException;
import com.pollite.exception.SurveyNotActiveException;
import com.pollite.model.survey.Survey;
import com.pollite.model.survey.SurveyQuestionExclusion;
import com.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompletedSurveyValidator {

    private final SurveyRepository surveyRepository;
    private final Clock clok;

    private Survey survey;

    public void validate(CompletedSurveyDto completedSurveyDto)
            throws SurveyNotActiveException, SurveyDoesNotExistException, InvalidCompletedSurveyException {
        var survey = surveyRepository.findById(completedSurveyDto.getSurveyId())
                .orElseThrow(() -> new SurveyDoesNotExistException(completedSurveyDto.getSurveyId()));

        if (!isSurveyActive(survey)) {
            throw new SurveyNotActiveException(survey.getId());
        }

        this.survey = survey;

        var completedQuestions = completedSurveyDto.getCompletedQuestions();

        var answersCorrect = completedQuestions.stream().allMatch(this::isCompletedQuestionValid);

        if (!answersCorrect || !answersMatchQuestions(completedQuestions)) {
            throw new InvalidCompletedSurveyException();
        }
    }

    private boolean answersMatchQuestions(List<CompletedSurveyQuestionDto> completedQuestions) {
        var exclusions = survey.getConfiguration().getExclusions();

        var questionsIdsRequiringAnswer = survey.getQuestions().stream()
                .map(SurveyQuestion::getId)
                .collect(Collectors.toSet());

        var questionsIdsToExclude = exclusions.stream()
                .filter(exclusion -> isExclusionApplicable(exclusion, completedQuestions))
                .map(SurveyQuestionExclusion::getExcludedQuestionOrder)
                .map(order -> survey.getQuestionByOrder(order))
                .map(SurveyQuestion::getId)
                .collect(Collectors.toSet());

        questionsIdsRequiringAnswer.removeAll(questionsIdsToExclude);

        var answeredQuestionsIds = completedQuestions.stream()
                .map(CompletedSurveyQuestionDto::getQuestionId)
                .collect(Collectors.toSet());

        return questionsIdsRequiringAnswer.equals(answeredQuestionsIds);
    }

    private boolean isExclusionApplicable(SurveyQuestionExclusion exclusion,
                                          List<CompletedSurveyQuestionDto> completedQuestions) {
        var question = survey.getQuestionByOrder(exclusion.getQuestionOrder());
        var expectedAnswer = question.getAnswerByOrder(exclusion.getAnswerOrder());

        var foundCompletedQuestionOptional = completedQuestions.stream()
                .filter(completedQuestion -> Objects.equals(completedQuestion.getQuestionId(), question.getId()))
                .findFirst();

        if (foundCompletedQuestionOptional.isEmpty()) {
            return false;
        }

        var foundCompletedQuestion = foundCompletedQuestionOptional.get();

        return foundCompletedQuestion.getQuestionAnswerIds().contains(expectedAnswer.getId());
    }

    private boolean isCompletedQuestionValid(CompletedSurveyQuestionDto completedSurveyQuestionDto) {
        var answerIds = completedSurveyQuestionDto.getQuestionAnswerIds();
        var questionOptional = survey.getQuestions().stream()
                .filter(surveyQuestion -> surveyQuestion.getId().equals(completedSurveyQuestionDto.getQuestionId()))
                .findFirst();

        if (questionOptional.isEmpty()) {
            return false;
        }

        var question = questionOptional.get();

        return isAnswersAmountCorrect(question.getType(), answerIds.size())
                && everyAnswerExists(question.getAnswers(), answerIds);
    }

    private boolean isAnswersAmountCorrect(SurveyQuestionType questionType, int answersAmount) {
        return switch (questionType) {
            case SINGLE_CHOICE -> answersAmount == 1;
            case MULTI_CHOICE -> answersAmount > 0;
        };
    }

    private boolean everyAnswerExists(List<SurveyQuestionAnswer> answers, Set<Long> answerIds) {
        return answers.stream()
                .map(SurveyQuestionAnswer::getId)
                .collect(Collectors.toSet())
                .containsAll(answerIds);
    }

    private boolean isSurveyActive(Survey survey) {
        var startDate = survey.getConfiguration().getStartDate();
        var endDate = survey.getConfiguration().getEndDate().plusDays(1).minusSeconds(1);
        var now = OffsetDateTime.now(clok);
        var isActive = survey.getConfiguration().getIsActive();

        return isActive && surveyHasStarted(startDate, now) && !surveyHasEnded(endDate, now);
    }

    private boolean surveyHasStarted(OffsetDateTime startDate, OffsetDateTime now) {
        return startDate == null || now.isEqual(startDate) || now.isAfter(startDate);
    }

    private boolean surveyHasEnded(OffsetDateTime endDate, OffsetDateTime now) {
        return endDate != null && now.isAfter(endDate);
    }
}
