package com.messenger.model;

import com.messenger.enums.UserRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private String firstName = "firstName";
    private String lastName = "lastName";
    private String email = "email@email.com";
    private String password = "secret";
    private UserRole userRole = UserRole.NORMAL;
    private String id = "000000000000000000000000";

    @Test
    public void testGetId() {
        User user = new User();
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testGetFirstName() {
        User user = new User(firstName, lastName, email, password, userRole);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        User user = new User(firstName, lastName, email, password, userRole);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void testGetEmail() {
        User user = new User(firstName, lastName, email, password, userRole);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetPassword() {
        User user = new User(firstName, lastName, email, password, userRole);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void getUserRole() {
        User user = new User(firstName, lastName, email, password, userRole);
        assertEquals(userRole, user.getUserRole());
    }

    @Test
    public void setId() {
        User user = new User();
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testSetFirstName() {
        User user = new User();
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    public void testSetLastName() {
        User user = new User();
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void testSetEmail() {
        User user = new User();
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetUserRole() {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }

}