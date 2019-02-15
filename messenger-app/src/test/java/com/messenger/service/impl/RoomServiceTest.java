package com.messenger.service.impl;

import com.messenger.exception.RoomNotFoundException;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.repository.IRoomRepository;
import com.messenger.service.IRoomService;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoomServiceTest {

    private IRoomRepository roomRepositoryMock;
    private IRoomService serviceToTest;

    private Room room;
    private List<User> participants;
    private String id;

    @Before
    public void setUp() {
        this.roomRepositoryMock = Mockito.mock(IRoomRepository.class);
        this.serviceToTest = new RoomService(roomRepositoryMock);

        this.id = "000000000000000000000000";
        this.participants = new LinkedList<>(Arrays.asList(
                new User("Ben", "Domu", "ben@email.com", "secret", UserRole.NORMAL),
                new User("John", "Doe", "john@email.com", "secret", UserRole.ADMIN),
                new User("Diu", "Unb", "unb@email.com", "secret", UserRole.NORMAL),
                new User("Loc", "Lowe", "loc@email.com", "secret", UserRole.AUDITOR)
        ));
        this.room = new Room("Pals", this.participants);
        this.room.setId(this.id);
    }

    @Test
    public void testInsert() {
        Room roomToInsert = new Room(room.getName(), room.getParticipants());
        doAnswer((Answer<Void>) invocation -> {
            Room room = invocation.getArgument(0);
            room.setId(id);
            return null;
        }).when(roomRepositoryMock).insert(any(Room.class));

        serviceToTest.insert(roomToInsert);

        assertNotNull(roomToInsert.getId());
        verify(roomRepositoryMock, times(1)).insert(any(Room.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertInvalidValuesThrowsException() {
        Room invalidRoom = new Room("", Collections.emptyList());
        serviceToTest.insert(invalidRoom);
    }

    @Test
    public void testUpdate() {
        Room newRoom = new Room("newName", room.getParticipants());
        Room expectedRoom = new Room("newName", room.getParticipants());
        expectedRoom.setId(id);
        when(roomRepositoryMock.findById(id)).thenReturn(Optional.of(room));
        Room actual = serviceToTest.update(id, newRoom);
        assertEquals(expectedRoom, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateInvalidValuesThrowsException() {
        Room invalidRoom = new Room(null, room.getParticipants());
        serviceToTest.update(id, invalidRoom);
    }

    @Test(expected = RoomNotFoundException.class)
    public void testUpdateNonExistentUser() {
        doThrow(RoomNotFoundException.class).when(roomRepositoryMock).findById(id);
        serviceToTest.update(id, room);
    }

    @Test
    public void testDelete() {
        when(roomRepositoryMock.findById(anyString())).thenReturn(Optional.of(room));
        serviceToTest.delete(id);
        verify(roomRepositoryMock, times(1)).delete(any(Room.class));
    }

    @Test
    public void testFind() {
        when(roomRepositoryMock.findById(anyString())).thenReturn(Optional.of(room));
        Room actual = serviceToTest.find(id);
        assertEquals(room, actual);
    }

    @Test(expected = RoomNotFoundException.class)
    public void testFindNonExistentId() {
        doThrow(RoomNotFoundException.class).when(roomRepositoryMock).findById(id);
        serviceToTest.find(id);
    }

    @Test
    public void testFindAll() {
        Page<Room> mockReturn = new PageImpl<>(Collections.singletonList(room));
        when(roomRepositoryMock.findAll(any(Pageable.class))).thenReturn(mockReturn);
        Page<Room> actual = serviceToTest.findAll(0);
        assertEquals(mockReturn, actual);
    }

    @Test
    public void testFindUsersRooms() {
        User user = room.getParticipants().get(0);
        when(roomRepositoryMock.findAllByParticipantsIsContaining(user)).thenReturn(Collections.singletonList(room));
        List<Room> rooms = serviceToTest.findUsersRooms(user);
        assertTrue(rooms.size() > 0);
    }

    @Test
    public void testFindAllByMessages() {
        User sender = room.getParticipants().get(0);
        List<User> receivers = Collections.singletonList(room.getParticipants().get(1));
        Message newMessage = new Message(sender, receivers, "Hello");
        List<Message> messages = new ArrayList<>(Collections.singletonList(newMessage));
        when(roomRepositoryMock.findAllByMessagesIsContaining(anyList())).thenReturn(Collections.singletonList(room));
        List<Room> rooms = serviceToTest.findAllByMessages(messages);
        assertTrue(rooms.size() > 0);
    }

    @Test
    public void testRemoveParticipantFromRoom() {
        String removeThisId = "11111111111111111111";
        room.getParticipants().get(0).setId("00000000000000000000");
        room.getParticipants().get(1).setId(removeThisId);
        room.getParticipants().get(2).setId("22222222222222222222");
        room.getParticipants().get(3).setId("33333333333333333333");

        when(roomRepositoryMock.findById(anyString())).thenReturn(Optional.of(room));

        Room actual = serviceToTest.removeParticipantFromRoom(id, removeThisId);

        assertEquals(3, actual.getParticipants().size());
        verify(roomRepositoryMock, times(1)).save(any(Room.class));

    }
}