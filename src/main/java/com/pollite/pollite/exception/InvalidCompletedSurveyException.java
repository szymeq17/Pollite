package com.pollite.pollite.exception;

public class InvalidCompletedSurveyException extends Exception {

    public InvalidCompletedSurveyException() {
        super("Completed survey is invalid!");
    }
}
