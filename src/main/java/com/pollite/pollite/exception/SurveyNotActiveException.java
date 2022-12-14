package com.pollite.pollite.exception;

public class SurveyNotActiveException extends Exception {

    public SurveyNotActiveException(Long id) {
        super(String.format("Survey with id=%d is not active!", id));
    }
}
