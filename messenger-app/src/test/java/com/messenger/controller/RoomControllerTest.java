package com.messenger.controller;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.service.MessageService;
import com.messenger.service.RoomService;
import com.messenger.service.UserService;
import com.messenger.utils.SearchType;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RoomControllerTest {

    private RoomService roomServiceMock;
    private MessageService messageServiceMock;
    private UserService userServiceMock;
    private RoomController controllerToTest;
    private Room room;
    private List<User> participants;
    private String id;

    @Before
    public void setUp() {
        this.roomServiceMock = Mockito.mock(RoomService.class);
        this.messageServiceMock = Mockito.mock(MessageService.class);
        this.userServiceMock = Mockito.mock(UserService.class);
        this.controllerToTest = new RoomController(roomServiceMock, messageServiceMock, userServiceMock);

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
        Room roomToCreate = new Room(room.getName(), room.getParticipants());
        Room roomToReturn = new Room(room.getName(), room.getParticipants());
        roomToReturn.setId(id);
        when(roomServiceMock.insert(any(Room.class))).thenReturn(roomToReturn);
        ResponseEntity actual = controllerToTest.create(roomToCreate);
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
    public void testSearch() {
        User sender = room.getParticipants().get(0);
        List<User> receivers = Collections.singletonList(room.getParticipants().get(1));
        Message newMessage = new Message(sender, receivers, "Hello");

        when(messageServiceMock.search(any(SearchType.class), anyString())).thenReturn(Collections.singletonList(newMessage));
        when(roomServiceMock.findAllByMessages(Collections.singletonList(newMessage))).thenReturn(Collections.singletonList(room));

        List<Room> actual = controllerToTest.search(SearchType.CONTENT, "Hello").getBody();
        assertEquals(Collections.singletonList(room), actual);
    }

    @Test
    public void testGetUsersRooms() {
        User participant = room.getParticipants().get(0);
        when(userServiceMock.find(id)).thenReturn(participant);
        when(roomServiceMock.findUsersRooms(participant)).thenReturn(Collections.singletonList(room));

        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body(Collections.singletonList(room));
        ResponseEntity actual = controllerToTest.findUsersRooms(id);
        assertEquals(expected, actual);
    }
}
