package com.messenger.service;

import com.messenger.exception.MessageNotFoundException;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.enums.SearchType;

import java.util.List;

public interface MessageService {

    Message sendMessage(final Room room, final Message message);

    Message find(String id) throws MessageNotFoundException;

    List<Message> findAll();

    List<Message> search(final SearchType searchType, final String searchParam);
}
