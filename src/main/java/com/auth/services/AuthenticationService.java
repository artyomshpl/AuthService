package com.auth.services;

import com.auth.dto.JwtAuthenticationResponse;
import com.auth.dto.SignInRequest;
import com.auth.dto.SignUpRequest;
import com.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        try {
            User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getRole());
            userService.save(user);
            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Error during sign up", e);
        }
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            logger.info("Attempting to authenticate user: {}", request.getUsername(), request.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
            logger.info("User authenticated successfully: {}", request.getUsername());

            var user = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getUsername());
            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found: {}", request.getUsername(), e);
            throw new UsernameNotFoundException("User not found");
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: {}", request.getUsername(), e);
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            logger.error("Error during sign in for user: {}", request.getUsername(), e);
            throw new RuntimeException("Error during sign in", e);
        }
    }
}

