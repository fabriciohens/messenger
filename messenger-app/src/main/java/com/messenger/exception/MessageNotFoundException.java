package com.messenger.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(String id) {
        super("Message not found for id: " + id);
    }
}
