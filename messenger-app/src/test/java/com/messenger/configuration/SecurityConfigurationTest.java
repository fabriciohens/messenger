package com.messenger.configuration;

import com.messenger.service.impl.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SecurityConfigurationTest {

    private UserDetailsServiceImpl userDetailServiceMock;
    private SecurityConfiguration configToTest;

    @Before
    public void setUp() {
        this.userDetailServiceMock = mock(UserDetailsServiceImpl.class);
        this.configToTest = new SecurityConfiguration(userDetailServiceMock);
    }


    // TODO: implement this test
    // @Test
    // public void testConfigureHttpSecurity() throws Exception {
    //    fail("Test not implemented");
    //}

    @Test
    public void testConfigureAuthenticationManagerBuilder() throws Exception {
        AuthenticationManagerBuilder builderMock = mock(AuthenticationManagerBuilder.class);
        configToTest.configure(builderMock);
        verify(builderMock, only()).userDetailsService(userDetailServiceMock);
    }
}