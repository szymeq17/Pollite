package com.pollite.dto;

import com.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.model.survey.question.CompletedQuestion;
import com.pollite.model.survey.CompletedSurvey;
import lombok.Data;

import java.util.function.Predicate;

@Data
public class CompletedSurveyFilter {
    private Long questionId;
    private Long answerId;

    public Predicate<CompletedSurvey> toPredicate() {
        return completedSurvey -> completedSurvey.getCompletedQuestions()
                .stream()
                .anyMatch(this::doesAnswerMatch);
    }

    private boolean doesAnswerMatch(CompletedQuestion completedQuestion) {
        var questionId = completedQuestion.getQuestion().getId();
        var answerIds = completedQuestion.getAnswers()
                .stream()
                .map(SurveyQuestionAnswer::getId)
                .toList();

        return this.questionId.equals(questionId) && answerIds.contains(this.answerId);
    }
}
