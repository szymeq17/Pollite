package com.pollite.exception;

public class SurveyDoesNotExistException extends RuntimeException {

    public SurveyDoesNotExistException(Long id) {
        super(String.format("Survey with id=%d does not exist!", id));
    }
}
