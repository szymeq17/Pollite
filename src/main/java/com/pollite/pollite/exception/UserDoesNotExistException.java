package com.pollite.pollite.exception;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String username) {
        super(String.format("User '%s' does not exist!", username));
    }
}
