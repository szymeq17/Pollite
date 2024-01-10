package com.pollite.exception;

public class PollAnswerDoesNotExistException extends RuntimeException {
    public PollAnswerDoesNotExistException(Long id) {
        super(String.format("Answer with id=%d does not exist!", id));
    }
}
