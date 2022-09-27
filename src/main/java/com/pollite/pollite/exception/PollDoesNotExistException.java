package com.pollite.pollite.exception;

public class PollDoesNotExistException extends Exception {
    public PollDoesNotExistException(Long id) {
        super(String.format("Poll with id=%d does not exist!", id));
    }
}
