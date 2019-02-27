package com.messenger.model;

import com.messenger.enums.UserRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public @Data class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole userRole;

    public User() {
    }

    public User(final String firstName, final String lastName, final String email, final String password, final UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
