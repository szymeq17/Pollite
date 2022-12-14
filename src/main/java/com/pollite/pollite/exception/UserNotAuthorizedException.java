package com.pollite.pollite.exception;

public class UserNotAuthorizedException extends Exception {
    public UserNotAuthorizedException(String username) {
        super(String.format("User '%s' is not authorized for this operation!", username));
    }
}
