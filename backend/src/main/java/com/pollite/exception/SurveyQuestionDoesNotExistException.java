package com.pollite.exception;

public class SurveyQuestionDoesNotExistException extends RuntimeException {
    public SurveyQuestionDoesNotExistException(Long id) {
        super(String.format("Question with id=%d does not exist!", id));
    }
}
