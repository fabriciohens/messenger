package com.messenger.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "messages")
public @Data class Message {

    @Id
    private String id;

    private String content;

    @DBRef
    private User sender;

    @DBRef
    private List<User> receivers;

    public Message() {
    }

    public Message(final User sender, final List<User> receivers, final String content) {
        this.sender = sender;
        this.receivers = receivers;
        this.content = content;
    }
}
