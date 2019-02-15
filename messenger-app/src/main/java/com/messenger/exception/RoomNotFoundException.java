package com.messenger.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(final String id) {
        super("Room not found for id: " + id);
    }
}
