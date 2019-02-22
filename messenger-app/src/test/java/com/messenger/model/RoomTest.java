package com.messenger.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class RoomTest {

    private String name;
    private String id;
    private List<User> participants;

    @Before
    public void setUp() {
        this.name = "RoomName";
        this.id = "000000000000000000000000";
        this.participants = Collections.singletonList(new User());
    }

    @Test
    public void testGetId() {
        Room room = new Room();
        room.setId(id);
        assertEquals(id, room.getId());
    }

    @Test
    public void testGetName() {
        Room room = new Room(name, participants);
        assertEquals(name, room.getName());
    }

    @Test
    public void testGetParticipants() {
        Room room = new Room(name, participants);
        assertEquals(participants, room.getParticipants());
    }

    @Test
    public void testGetMessages() {
        List<Message> msgs = Collections.singletonList(new Message());
        Room room = new Room();
        room.setMessages(msgs);
        assertEquals(msgs, room.getMessages());
    }

    @Test
    public void testSetId() {
        Room room = new Room();
        room.setId(id);
        assertEquals(id, room.getId());
    }

    @Test
    public void testSetName() {
        Room room = new Room();
        room.setName(name);
        assertEquals(name, room.getName());
    }

    @Test
    public void testSetParticipants() {
        Room room = new Room();
        room.setParticipants(participants);
        assertEquals(participants, room.getParticipants());
    }

    @Test
    public void testSetMessages() {
        List<Message> msgs = Collections.singletonList(new Message());
        Room room = new Room();
        room.setMessages(msgs);
        assertEquals(msgs, room.getMessages());
    }
}