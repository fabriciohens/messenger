package com.messenger.controller;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.service.IMessageService;
import com.messenger.service.IRoomService;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MessageControllerTest {

    private IRoomService roomServiceMock;
    private IMessageService messageServiceMock;
    private MessageController controllerToTest;
    private Message message;
    private User sender;
    private List<User> receivers;
    private String id;

    @Before
    public void setUp() {
        this.roomServiceMock = Mockito.mock(IRoomService.class);
        this.messageServiceMock = Mockito.mock(IMessageService.class);
        this.controllerToTest = new MessageController(roomServiceMock, messageServiceMock);

        this.id = "000000000000000000000000";
        sender = new User("Ben", "Domu", "ben@email.com", "secret", UserRole.NORMAL);
        receivers = Arrays.asList(
                new User("John", "Doe", "john@email.com", "secret", UserRole.ADMIN),
                new User("Diu", "Unb", "unb@email.com", "secret", UserRole.NORMAL),
                new User("Loc", "Lowe", "loc@email.com", "secret", UserRole.AUDITOR)
        );
        this.message = new Message(sender, receivers, "Hello");
        this.message.setId(this.id);
    }

    @Test
    public void testGetMessage() {
        when(messageServiceMock.find(id)).thenReturn(message);
        ResponseEntity actual = controllerToTest.find(id);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(message), actual);
    }

    @Test
    public void testGetAllMessages() {
        List<Message> expected = Collections.singletonList(message);
        when(messageServiceMock.findAll()).thenReturn(expected);
        List<Message> actual = controllerToTest.findAll().getBody();
        assertEquals(expected, actual);
    }

    @Test
    public void testSendMessage() {
        List<User> participants = new ArrayList<>(receivers);
        participants.add(sender);
        Room room = new Room("Pals", participants);
        Message newMessage = new Message(message.getSender(), message.getReceivers(), message.getContent());
        when(roomServiceMock.find(anyString())).thenReturn(room);
        when(messageServiceMock.sendMessage(any(Room.class), any(Message.class))).thenReturn(message);

        ResponseEntity expected = ResponseEntity.status(HttpStatus.CREATED).body(message);
        ResponseEntity actual = controllerToTest.sendMessageInRoom(id, newMessage);

        assertEquals(expected, actual);
    }

}
