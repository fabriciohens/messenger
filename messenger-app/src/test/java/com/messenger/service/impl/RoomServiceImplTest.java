package com.messenger.service.impl;

import com.messenger.exception.RoomNotFoundException;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.repository.RoomRepository;
import com.messenger.service.RoomService;
import com.messenger.enums.UserRole;
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

public class RoomServiceImplTest {

    private RoomRepository roomRepository;
    private RoomService roomService;

    private Room room;
    private List<User> participants;
    private String id;

    @Before
    public void setUp() {
        this.roomRepository = Mockito.mock(RoomRepository.class);
        this.roomService = new RoomServiceImpl(roomRepository);

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
        }).when(roomRepository).insert(any(Room.class));

        roomService.insert(roomToInsert);

        assertNotNull(roomToInsert.getId());
        verify(roomRepository, times(1)).insert(any(Room.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertInvalidValuesThrowsException() {
        Room invalidRoom = new Room("", Collections.emptyList());
        roomService.insert(invalidRoom);
    }

    @Test
    public void testUpdate() {
        Room newRoom = new Room("newName", room.getParticipants());
        Room expectedRoom = new Room("newName", room.getParticipants());
        expectedRoom.setId(id);
        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Room actual = roomService.update(id, newRoom);
        assertEquals(expectedRoom, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateInvalidValuesThrowsException() {
        Room invalidRoom = new Room(null, room.getParticipants());
        roomService.update(id, invalidRoom);
    }

    @Test(expected = RoomNotFoundException.class)
    public void testUpdateNonExistentUser() {
        doThrow(RoomNotFoundException.class).when(roomRepository).findById(id);
        roomService.update(id, room);
    }

    @Test
    public void testDelete() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        roomService.delete(id);
        verify(roomRepository, times(1)).delete(any(Room.class));
    }

    @Test
    public void testFind() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        Room actual = roomService.find(id);
        assertEquals(room, actual);
    }

    @Test(expected = RoomNotFoundException.class)
    public void testFindNonExistentId() {
        doThrow(RoomNotFoundException.class).when(roomRepository).findById(id);
        roomService.find(id);
    }

    @Test
    public void testFindAll() {
        Page<Room> mockReturn = new PageImpl<>(Collections.singletonList(room));
        when(roomRepository.findAll(any(Pageable.class))).thenReturn(mockReturn);
        Page<Room> actual = roomService.findAll(0);
        assertEquals(mockReturn, actual);
    }

    @Test
    public void testFindUsersRooms() {
        User user = room.getParticipants().get(0);
        when(roomRepository.findAllByParticipantsIsContaining(user)).thenReturn(Collections.singletonList(room));
        List<Room> rooms = roomService.findUsersRooms(user);
        assertTrue(rooms.size() > 0);
    }

    @Test
    public void testFindAllByMessages() {
        User sender = room.getParticipants().get(0);
        List<User> receivers = Collections.singletonList(room.getParticipants().get(1));
        Message newMessage = new Message(sender, receivers, "Hello");
        List<Message> messages = new ArrayList<>(Collections.singletonList(newMessage));
        when(roomRepository.findAllByMessagesIsContaining(anyList())).thenReturn(Collections.singletonList(room));
        List<Room> rooms = roomService.findAllByMessages(messages);
        assertTrue(rooms.size() > 0);
    }

    @Test
    public void testRemoveParticipantFromRoom() {
        String removeThisId = "11111111111111111111";
        room.getParticipants().get(0).setId("00000000000000000000");
        room.getParticipants().get(1).setId(removeThisId);
        room.getParticipants().get(2).setId("22222222222222222222");
        room.getParticipants().get(3).setId("33333333333333333333");

        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));

        Room actual = roomService.removeParticipantFromRoom(id, removeThisId);

        assertEquals(3, actual.getParticipants().size());
        verify(roomRepository, times(1)).save(any(Room.class));

    }
}