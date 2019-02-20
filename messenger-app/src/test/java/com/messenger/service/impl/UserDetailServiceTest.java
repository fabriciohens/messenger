package com.messenger.service.impl;

import com.messenger.model.User;
import com.messenger.repository.IUserRepository;
import com.messenger.utils.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailServiceTest {

    private IUserRepository userRepositoryMock;
    private MyUserDetailsService serviceToTest;

    @Before
    public void setUp() {
        this.userRepositoryMock = mock(IUserRepository.class);
        this.serviceToTest = new MyUserDetailsService(userRepositoryMock);
    }

    @Test
    public void testLoadUserByUsername() {
        User user = new User("FirstName", "LastName", "email@email@.com", "secret", UserRole.NORMAL);
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(user);
        UserDetails userDetail = serviceToTest.loadUserByUsername(user.getEmail());
        assertNotNull(userDetail);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameNonExistentEmail() {
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(null);
        serviceToTest.loadUserByUsername("NonExistentEmail");
    }
}