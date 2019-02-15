package com.messenger.controller;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.service.IMessageService;
import com.messenger.service.IRoomService;
import com.messenger.utils.SearchType;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RoomControllerTest {

    private IRoomService roomServiceMock;
    private IMessageService messageService;
    private RoomController controllerToTest;
    private Room room;
    private List<User> participants;
    private String id;

    @Before
    public void setUp() {
        this.roomServiceMock = Mockito.mock(IRoomService.class);
        this.messageService = Mockito.mock(IMessageService.class);
        this.controllerToTest = new RoomController(roomServiceMock, messageService);

        this.id = "000000000000000000000000";
        this.participants = Arrays.asList(
                new User("Ben", "Domu", "ben@email.com", "secret", UserRole.NORMAL),
                new User("John", "Doe", "john@email.com", "secret", UserRole.ADMIN),
                new User("Diu", "Unb", "unb@email.com", "secret", UserRole.NORMAL),
                new User("Loc", "Lowe", "loc@email.com", "secret", UserRole.AUDITOR)
        );
        this.room = new Room("Pals", this.participants);
        this.room.setId(this.id);
    }

    @Test
    public void testCreateRoomValidValues() {
        Room newRoom = new Room(room.getName(), room.getParticipants());
        doAnswer((Answer<Void>) invocation -> {
            Room room = invocation.getArgument(0);
            room.setId(id);
            return null;
        }).when(roomServiceMock).insert(any(Room.class));
        ResponseEntity actual = controllerToTest.create(newRoom);
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(room), actual);
    }

    @Test
    public void testUpdateRoomValidValues() {
        Room newRoom = new Room("newName", room.getParticipants());
        when(roomServiceMock.update(id, newRoom)).thenReturn(newRoom);
        ResponseEntity actual = controllerToTest.update(id, newRoom);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(newRoom), actual);
    }

    @Test
    public void testDeleteRoom() {
        HttpStatus actual = controllerToTest.delete(id).getStatusCode();
        assertEquals(HttpStatus.OK, actual);
    }

    @Test
    public void testGetRoom() {
        when(roomServiceMock.find(id)).thenReturn(room);
        ResponseEntity actual = controllerToTest.find(id);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(room), actual);
    }

    @Test
    public void testGetAllRooms() {
        int numPage = 0;
        Page<Room> expected = new PageImpl<>(Collections.singletonList(room));
        when(roomServiceMock.findAll(numPage)).thenReturn(expected);
        Page<Room> actual = controllerToTest.findAllRooms(numPage).getBody();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveParticipant() {
        String idParticipant = "111111111111111111111111";
        room.getParticipants().get(0).setId(idParticipant);
        List<User> expectedParticipants = Arrays.asList(participants.get(1), participants.get(2));
        Room expectedRoom = new Room(room.getName(), expectedParticipants);
        expectedRoom.setId(id);
        when(roomServiceMock.removeParticipantFromRoom(id, idParticipant)).thenReturn(expectedRoom);
        Room actual = controllerToTest.removeParticipant(id, idParticipant).getBody();

        assertEquals(expectedRoom, actual);
    }

    @Test
    public void testSendMessage() {
        User sender = room.getParticipants().get(0);
        List<User> receivers = Collections.singletonList(room.getParticipants().get(1));
        Message newMessage = new Message(sender, receivers, "Hello");
        Message expectedMessage = new Message(sender, receivers, "Hello");
        expectedMessage.setId(id);

        when(roomServiceMock.find(anyString())).thenReturn(room);
        doAnswer(invocationOnMock -> {
            Room room = invocationOnMock.getArgument(0);
            Message message = invocationOnMock.getArgument(1);
            message.setId(id);
            room.getMessages().add(message);
            return null;
        }).when(messageService).sendMessage(any(Room.class), any(Message.class));

        ResponseEntity expected = ResponseEntity.status(HttpStatus.CREATED).body(expectedMessage);
        ResponseEntity actual = controllerToTest.sendMessageInRoom(id, newMessage);

        assertEquals(expected, actual);
        assertTrue(room.getMessages().size() > 0);
    }

    @Test
    public void testSearch() {
        User sender = room.getParticipants().get(0);
        List<User> receivers = Collections.singletonList(room.getParticipants().get(1));
        Message newMessage = new Message(sender, receivers, "Hello");

        when(messageService.search(any(SearchType.class), anyString())).thenReturn(Collections.singletonList(newMessage));
        when(roomServiceMock.findAllByMessages(Collections.singletonList(newMessage))).thenReturn(Collections.singletonList(room));


        List<Room> actual = controllerToTest.search(SearchType.CONTENT, "Something").getBody();
        assertEquals(Collections.singletonList(room), actual);
    }
}
