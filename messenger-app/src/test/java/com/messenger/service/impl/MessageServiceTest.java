package com.messenger.service.impl;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.repository.IMessageRepository;
import com.messenger.repository.IRoomRepository;
import com.messenger.repository.IUserRepository;
import com.messenger.service.IMessageService;
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

public class MessageServiceTest {

    private IMessageRepository messageRepositoryMock;
    private IUserRepository userRepositoryMock;
    private IRoomRepository roomRepositoryMock;
    private IMessageService serviceToTest;

    private Room room;
    private Message message;
    private String id;

    @Before
    public void setUp() {
        this.messageRepositoryMock = Mockito.mock(IMessageRepository.class);
        this.userRepositoryMock = Mockito.mock(IUserRepository.class);
        this.roomRepositoryMock = Mockito.mock(IRoomRepository.class);
        this.serviceToTest = new MessageService(messageRepositoryMock, userRepositoryMock, roomRepositoryMock);

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
        serviceToTest.sendMessage(room, message);
        assertTrue(room.getMessages().size() > 0);
        verify(messageRepositoryMock, times(1)).insert(any(Message.class));
    }

    @Test
    public void testFind() {
        when(messageRepositoryMock.findById(anyString())).thenReturn(Optional.of(message));
        Message actual = serviceToTest.find(id);
        assertEquals(message, actual);
    }

    @Test
    public void testFindAll() {
        List<Message> mockReturn = Collections.singletonList(message);
        when(messageRepositoryMock.findAll()).thenReturn(mockReturn);
        List<Message> actual = serviceToTest.findAll();
        assertEquals(mockReturn, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageInvalidValue() {
        Message invalidMessage = new Message(message.getSender(), message.getReceivers(), "");
        serviceToTest.sendMessage(room, invalidMessage);
    }

    @Test
    public void testSearchBySender() {
        User user = room.getParticipants().get(0);
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(messageRepositoryMock.findAllBySenderEquals(user)).thenReturn(Collections.singletonList(message));
        List<Message> messagesFound = serviceToTest.search(SearchType.SENDER, id);
        assertTrue(messagesFound.size() > 0);
    }

    @Test
    public void testSearchBySenderNonExistentId() {
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        List<Message> messagesFound = serviceToTest.search(SearchType.SENDER, id);
        assertTrue(messagesFound.isEmpty());
    }

    @Test
    public void testSearchByReceiver() {
        User user = room.getParticipants().get(0);
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(messageRepositoryMock.findAllByReceiversIsContaining(user)).thenReturn(Collections.singletonList(message));
        List<Message> messagesFound = serviceToTest.search(SearchType.RECEIVER, id);
        assertTrue(messagesFound.size() > 0);
    }

    @Test
    public void testSearchByReceiverNonExistentId() {
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        List<Message> messagesFound = serviceToTest.search(SearchType.RECEIVER, id);
        assertTrue(messagesFound.isEmpty());
    }

    @Test
    public void testSearchByContent() {
        when(messageRepositoryMock.findAllByContentIsContaining(anyString())).thenReturn(Collections.singletonList(message));
        List<Message> messagesFound = serviceToTest.search(SearchType.CONTENT, "AnyContent");
        assertTrue(messagesFound.size() > 0);
    }
}