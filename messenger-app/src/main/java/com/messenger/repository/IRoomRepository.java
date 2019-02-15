package com.messenger.repository;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IRoomRepository extends MongoRepository<Room, String> {

    List<Room> findAllByParticipantsIsContaining(User participant);

    List<Room> findAllByMessagesIsContaining(List<Message> messages);

}
