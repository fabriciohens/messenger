package com.messenger.repository;

import com.messenger.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserRepository extends MongoRepository<User, String> {

    User findFirstByFirstName(String firstName);

    User findByEmail(String email);
}
