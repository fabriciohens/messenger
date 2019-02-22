package com.messenger.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MessageTest {

    private String content;
    private String id;
    private User sender;
    private List<User> receivers;

    @Before
    public void setUp()  {
        this.content = "Hello";
        this.id = "000000000000000000000000";
        this.sender = new User();
        this.receivers = Collections.singletonList(new User());
    }

    @Test
    public void testGetId() {
        Message message = new Message();
        message.setId(id);
        assertEquals(id, message.getId());
    }

    @Test
    public void testGetContent() {
        Message message = new Message(sender, receivers, content);
        assertEquals(content, message.getContent());
    }

    @Test
    public void testGetSender() {
        Message message = new Message(sender, receivers, content);
        assertEquals(sender, message.getSender());
    }

    @Test
    public void testGetReceivers() {
        Message message = new Message(sender, receivers, content);
        assertEquals(receivers, message.getReceivers());
    }

    @Test
    public void testSetId() {
        Message message = new Message();
        message.setId(id);
        assertEquals(id, message.getId());
    }

    @Test
    public void testSetContent() {
        Message message = new Message();
        message.setContent(content);
        assertEquals(content, message.getContent());
    }

    @Test
    public void testSetSender() {
        Message message = new Message();
        message.setSender(sender);
        assertEquals(sender, message.getSender());
    }

    @Test
    public void testSetReceivers() {
        Message message = new Message();
        message.setReceivers(receivers);
        assertEquals(receivers, message.getReceivers());
    }
}