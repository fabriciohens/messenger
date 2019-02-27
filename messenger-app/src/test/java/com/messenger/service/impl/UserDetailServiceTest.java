package com.messenger.service.impl;

import com.messenger.model.User;
import com.messenger.repository.UserRepository;
import com.messenger.enums.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailServiceTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        User user = new User("FirstName", "LastName", "email@email@.com", "secret", UserRole.NORMAL);
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        UserDetails userDetail = userDetailsService.loadUserByUsername(user.getEmail());
        assertNotNull(userDetail);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameNonExistentEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        userDetailsService.loadUserByUsername("NonExistentEmail");
    }
}