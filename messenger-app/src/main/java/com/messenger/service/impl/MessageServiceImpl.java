package com.messenger.service.impl;

import com.messenger.exception.MessageNotFoundException;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.repository.MessageRepository;
import com.messenger.repository.RoomRepository;
import com.messenger.repository.UserRepository;
import com.messenger.service.MessageService;
import com.messenger.enums.SearchType;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public MessageServiceImpl(final MessageRepository messageRepository, final UserRepository userRepository, final RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Message sendMessage(final Room room, final Message message) {
        checkIfMessageIsValid(message);
        Message sentMessage = messageRepository.insert(message);

        room.getMessages().add(sentMessage);
        roomRepository.save(room);
        return sentMessage;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message find(final String id) throws MessageNotFoundException {
        return messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException(id));
    }

    @Override
    public List<Message> search(final SearchType searchType, final String searchParam) {
        if (searchType.equals(SearchType.SENDER))
            return searchBySender(searchParam);

        else if (searchType.equals(SearchType.RECEIVER))
            return searchByReceiver(searchParam);

        else
            return searchByContent(searchParam);

    }

    private List<Message> searchBySender(final String idSender) {
        Optional<User> sender = userRepository.findById(idSender);

        if (!sender.isPresent())
            return Collections.emptyList();

        return messageRepository.findAllBySenderEquals(sender.get());
    }

    private List<Message> searchByReceiver(final String idReceiver) {
        Optional<User> receiver = userRepository.findById(idReceiver);

        if (!receiver.isPresent())
            return Collections.emptyList();

        return messageRepository.findAllByReceiversIsContaining(receiver.get());
    }

    private List<Message> searchByContent(final String messageContent) {
        return messageRepository.findAllByContentIsContaining(messageContent);
    }

    private void checkIfMessageIsValid(final Message message) {
        StringBuilder errors = new StringBuilder();

        if (StringUtils.isEmpty(message.getContent())) {
            errors.append("Content cannot be empty.");
        }

        if (!StringUtils.isEmpty(errors.toString())) {
            throw new IllegalArgumentException(errors.toString());
        }
    }
}
