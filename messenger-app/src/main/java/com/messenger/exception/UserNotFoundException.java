package com.messenger.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String id) {
        super("User not found for id: " + id);
    }
}
