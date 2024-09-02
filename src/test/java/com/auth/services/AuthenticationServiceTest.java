package com.auth.services;

import com.auth.dto.JwtAuthenticationResponse;
import com.auth.dto.SignInRequest;
import com.auth.dto.SignUpRequest;
import com.auth.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testUser");
        request.setPassword("password");
        request.setRole("USER");

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getRole());

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken");

        JwtAuthenticationResponse response = authenticationService.signUp(request);

        assertEquals("testToken", response.getToken());
    }

    @Test
    public void testSignIn() {
        SignInRequest request = new SignInRequest();
        request.setUsername("testUser");
        request.setPassword("password");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                request.getUsername(),
                request.getPassword(),
                new ArrayList<>()
        );

        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("testToken");

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        assertEquals("testToken", response.getToken());
    }
}