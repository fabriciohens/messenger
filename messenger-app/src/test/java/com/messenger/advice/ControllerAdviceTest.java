package com.messenger.advice;

import com.messenger.exception.MessageNotFoundException;
import com.messenger.exception.RoomNotFoundException;
import com.messenger.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ControllerAdviceTest {

    private ControllerAdvice advice;
    private String id;
    private String errorMessage;

    @Before
    public void setUp() {
        this.advice = new ControllerAdvice();
        this.id = "000000000000000000000000";
        this.errorMessage = "Error message";
    }

    @Test
    public void testUserNotFoundHandler() {
        String actual = advice.userNotFoundHandler(new UserNotFoundException(id)).getBody();
        assertNotNull(actual);
    }

    @Test
    public void testRoomNotFoundHandler() {
        String actual = advice.roomNotFoundHandler(new RoomNotFoundException(id)).getBody();
        assertNotNull(actual);
    }

    @Test
    public void testMessageNotFoundHandler() {
        String actual = advice.messageNotFoundHandler(new MessageNotFoundException(id)).getBody();
        assertNotNull(actual);
    }

    @Test
    public void testIllegalArgumentHandler() {
        HttpStatus actual = advice.illegalArgumentHandler(new IllegalArgumentException(errorMessage)).getStatusCode();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, actual);
    }

    @Test
    public void testExceptionHandler() {
        HttpStatus actual = advice.exceptionHandler(new Exception(errorMessage)).getStatusCode();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual);
    }
}