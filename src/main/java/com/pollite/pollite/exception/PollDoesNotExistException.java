package com.pollite.pollite.exception;

public class PollDoesNotExistException extends RuntimeException {
    public PollDoesNotExistException(Long id) {
        super(String.format("Poll with id=%d does not exist!", id));
    }
}
