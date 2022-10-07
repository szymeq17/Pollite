package com.pollite.pollite.exception;

public class PollNotActiveException extends Exception {

    public PollNotActiveException(Long id) {
        super(String.format("Poll with id=%d is not active!", id));
    }
}
