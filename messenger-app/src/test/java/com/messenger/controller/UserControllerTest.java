package com.messenger.controller;

import com.messenger.model.User;
import com.messenger.service.IUserService;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private IUserService userServiceMock;
    private UserController controllerToTest;
    private User user;
    private String id;

    @Before
    public void setUp() {
        this.userServiceMock = mock(IUserService.class);
        this.controllerToTest = new UserController(userServiceMock);

        this.id = "000000000000000000000000";
        this.user = new User("Ben", "Joli", "ben@email.com", "secret", UserRole.NORMAL);
        this.user.setId(this.id);
    }

    @Test
    public void testCreateUserValidValues() {
        User userToCreate = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        User userToReturn = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        userToReturn.setId(id);
        when(userServiceMock.insert(any(User.class))).thenReturn(userToReturn);
        ResponseEntity actual = controllerToTest.create(userToCreate);

        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(user), actual);
        verify(userServiceMock, times(1)).insert(any(User.class));
    }

    @Test
    public void testUpdateUserValidValues() {
        User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        when(userServiceMock.update(id, newUser)).thenReturn(user);
        ResponseEntity actual = controllerToTest.update(id, newUser);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(user), actual);
    }

    @Test
    public void testDeleteUser() {
        HttpStatus actual = controllerToTest.delete(id).getStatusCode();
        assertEquals(HttpStatus.OK, actual);
    }

    @Test
    public void testGetUser() {
        when(userServiceMock.find(id)).thenReturn(user);
        ResponseEntity actual = controllerToTest.find(id);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(user), actual);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Collections.singletonList(user);
        when(userServiceMock.findAll()).thenReturn(users);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body(users);
        ResponseEntity actual = controllerToTest.findAll();
        assertEquals(expected, actual);
    }

}