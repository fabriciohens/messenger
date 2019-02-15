package com.messenger.repository;

import com.messenger.model.Message;
import com.messenger.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IMessageRepository extends MongoRepository<Message, String> {

    List<Message> findAllByContentIsContaining(String content);

    List<Message> findAllBySenderEquals(User sender);

    List<Message> findAllByReceiversIsContaining(User receiver);

}
