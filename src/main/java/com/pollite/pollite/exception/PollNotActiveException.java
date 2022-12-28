package com.pollite.pollite.exception;

public class PollNotActiveException extends RuntimeException {

    public PollNotActiveException(Long id) {
        super(String.format("Poll with id=%d is not active!", id));
    }
}
