package com.pollite.exception;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String username) {
        super(String.format("User '%s' does not exist!", username));
    }
}
