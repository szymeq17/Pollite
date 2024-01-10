package com.pollite.pollite.exception;

public class InvalidCompletedSurveyException extends RuntimeException {

    public InvalidCompletedSurveyException() {
        super("Completed survey is invalid!");
    }
}
