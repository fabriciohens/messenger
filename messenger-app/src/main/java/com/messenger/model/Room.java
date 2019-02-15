package com.messenger.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
public @Data class Room {

    @Id
    private String id;

    private String name;

    @DBRef
    private List<User> participants;

    @DBRef
    private List<Message> messages;

    public Room() {
    }

    public Room(final String name, final List<User> participants) {
        this.name = name;
        this.participants = participants;
        this.messages = new ArrayList<>();
    }
}
