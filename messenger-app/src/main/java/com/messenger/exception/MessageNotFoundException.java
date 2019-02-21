package com.messenger.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(String id) {
        super("Room not found for id: " + id);
    }
}
