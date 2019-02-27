package com.messenger.service.impl;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.repository.MessageRepository;
import com.messenger.repository.RoomRepository;
import com.messenger.repository.UserRepository;
import com.messenger.service.MessageService;
import com.messenger.utils.SearchType;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MessageServiceImplTest {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private MessageService messageService;

    private Room room;
    private Message message;
    private String id;

    @Before
    public void setUp() {
        this.messageRepository = Mockito.mock(MessageRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.roomRepository = Mockito.mock(RoomRepository.class);
        this.messageService = new MessageServiceImpl(messageRepository, userRepository, roomRepository);

        List<User> participants = Arrays.asList(
                new User("FirstName", "LastName", "email@email.com", "secret", UserRole.NORMAL),
                new User("FirstName", "LastName", "email@email.com", "secret", UserRole.NORMAL)
        );
        this.room = new Room("NameRoom", participants);

        User sender = room.getParticipants().get(0);
        List<User> receivers = Collections.singletonList(room.getParticipants().get(1));
        this.message = new Message(sender, receivers, "Hello");
        this.id = "000000000000000000000000";
    }

    @Test
    public void testSendMessage() {
        messageService.sendMessage(room, message);
        assertTrue(room.getMessages().size() > 0);
        verify(messageRepository, times(1)).insert(any(Message.class));
    }

    @Test
    public void testFind() {
        when(messageRepository.findById(anyString())).thenReturn(Optional.of(message));
        Message actual = messageService.find(id);
        assertEquals(message, actual);
    }

    @Test
    public void testFindAll() {
        List<Message> mockReturn = Collections.singletonList(message);
        when(messageRepository.findAll()).thenReturn(mockReturn);
        List<Message> actual = messageService.findAll();
        assertEquals(mockReturn, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageInvalidValue() {
        Message invalidMessage = new Message(message.getSender(), message.getReceivers(), "");
        messageService.sendMessage(room, invalidMessage);
    }

    @Test
    public void testSearchBySender() {
        User user = room.getParticipants().get(0);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(messageRepository.findAllBySenderEquals(user)).thenReturn(Collections.singletonList(message));
        List<Message> messagesFound = messageService.search(SearchType.SENDER, id);
        assertTrue(messagesFound.size() > 0);
    }

    @Test
    public void testSearchBySenderNonExistentId() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        List<Message> messagesFound = messageService.search(SearchType.SENDER, id);
        assertTrue(messagesFound.isEmpty());
    }

    @Test
    public void testSearchByReceiver() {
        User user = room.getParticipants().get(0);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(messageRepository.findAllByReceiversIsContaining(user)).thenReturn(Collections.singletonList(message));
        List<Message> messagesFound = messageService.search(SearchType.RECEIVER, id);
        assertTrue(messagesFound.size() > 0);
    }

    @Test
    public void testSearchByReceiverNonExistentId() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        List<Message> messagesFound = messageService.search(SearchType.RECEIVER, id);
        assertTrue(messagesFound.isEmpty());
    }

    @Test
    public void testSearchByContent() {
        when(messageRepository.findAllByContentIsContaining(anyString())).thenReturn(Collections.singletonList(message));
        List<Message> messagesFound = messageService.search(SearchType.CONTENT, "AnyContent");
        assertTrue(messagesFound.size() > 0);
    }
}