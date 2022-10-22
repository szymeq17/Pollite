package com.pollite.pollite.exception;

public class SurveyDoesNotExistException extends Exception {

    public SurveyDoesNotExistException(Long id) {
        super(String.format("Survey with id=%d does not exist!", id));
    }
}
