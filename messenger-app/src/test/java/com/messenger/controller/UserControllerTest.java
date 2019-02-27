package com.messenger.controller;

import com.messenger.model.User;
import com.messenger.service.UserService;
import com.messenger.enums.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;
    private User user;
    private String id;

    @Before
    public void setUp() {
        this.userService = mock(UserService.class);
        this.userController = new UserController(userService);

        this.id = "000000000000000000000000";
        this.user = new User("Ben", "Joli", "ben@email.com", "secret", UserRole.NORMAL);
        this.user.setId(this.id);
    }

    @Test
    public void testCreateUserValidValues() {
        User userToCreate = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        User userToReturn = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        userToReturn.setId(id);
        when(userService.insert(any(User.class))).thenReturn(userToReturn);
        ResponseEntity actual = userController.create(userToCreate);

        assertEquals(ResponseEntity.status(CREATED).body(userToReturn), actual);
        verify(userService, times(1)).insert(any(User.class));
    }

    @Test
    public void testUpdateUserValidValues() {
        User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        when(userService.update(id, newUser)).thenReturn(user);
        ResponseEntity actual = userController.update(id, newUser);
        assertEquals(ResponseEntity.status(OK).body(user), actual);
    }

    @Test
    public void testDeleteUser() {
        HttpStatus actual = userController.delete(id).getStatusCode();
        assertEquals(OK, actual);
    }

    @Test
    public void testGetUser() {
        when(userService.find(id)).thenReturn(user);
        ResponseEntity actual = userController.find(id);
        assertEquals(ResponseEntity.status(OK).body(user), actual);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Collections.singletonList(user);
        when(userService.findAll()).thenReturn(users);
        ResponseEntity expected = ResponseEntity.status(OK).body(users);
        ResponseEntity actual = userController.findAll();
        assertEquals(expected, actual);
    }

}