package com.messenger.service.impl;

import com.messenger.exception.UserNotFoundException;
import com.messenger.model.User;
import com.messenger.repository.UserRepository;
import com.messenger.service.UserService;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserRepository userRepositoryMock;
    private UserService serviceToTest;

    private String id;
    private User user;

    @Before
    public void setUp() {
        this.userRepositoryMock = mock(UserRepository.class);
        this.serviceToTest = new UserServiceImpl(userRepositoryMock);

        this.id = "000000000000000000000000";
        this.user = new User("Ben", "Joli", "ben@email.com", "secret", UserRole.NORMAL);
        this.user.setId(this.id);
    }

    @Test
    public void testInsert() {
        User userToInsert = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        doAnswer((Answer<Void>) invocation -> {
            User user = invocation.getArgument(0);
            user.setId(id);
            return null;
        }).when(userRepositoryMock).insert(any(User.class));

        serviceToTest.insert(userToInsert);

        assertNotNull(userToInsert.getId());
        verify(userRepositoryMock, times(1)).insert(any(User.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertInvalidValuesThrowsException() {
        User invalidUser = new User("", "", "", "", null);
        serviceToTest.insert(invalidUser);
    }

    @Test
    public void testUpdate() {
        User newUser = new User("newFirstName", "newLastName", user.getEmail(), user.getPassword(), user.getUserRole());
        User expectedUser = new User("newFirstName", "newLastName", user.getEmail(), user.getPassword(), user.getUserRole());
        expectedUser.setId(id);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(user));

        User actual = serviceToTest.update(id, newUser);

        assertEquals(expectedUser, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateInvalidValuesThrowsException() {
        User invalidUser = new User("", "", "", "", null);
        serviceToTest.update(id, invalidUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateNonExistentUser() {
        doThrow(UserNotFoundException.class).when(userRepositoryMock).findById(id);
        serviceToTest.update(id, user);
    }

    @Test
    public void testDelete() {
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        serviceToTest.delete(id);
        verify(userRepositoryMock, times(1)).delete(any(User.class));
    }

    @Test
    public void testFind() {
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        User actual = serviceToTest.find(id);
        assertEquals(user, actual);
    }

    @Test(expected = UserNotFoundException.class)
    public void testFindNonExistentId() {
        doThrow(UserNotFoundException.class).when(userRepositoryMock).findById(id);
        serviceToTest.find(id);
    }

    @Test
    public void testFindAll() {
        List<User> mockReturn = Collections.singletonList(user);
        when(userRepositoryMock.findAll()).thenReturn(mockReturn);
        List<User> actual = serviceToTest.findAll();
        assertEquals(mockReturn, actual);
    }
}