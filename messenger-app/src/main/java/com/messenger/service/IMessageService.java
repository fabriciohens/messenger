package com.messenger.service;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.utils.SearchType;

import java.util.List;

public interface IMessageService {

    Message sendMessage(final Room room, final Message message);

    List<Message> search(final SearchType searchType, final String searchParam);
}
