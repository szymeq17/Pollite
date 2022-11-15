package com.pollite.pollite.exception;

public class SurveyQuestionDoesNotExistException extends Exception {
    public SurveyQuestionDoesNotExistException(Long id) {
        super(String.format("Question with id=%d does not exist!", id));
    }
}
