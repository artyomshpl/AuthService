package com.auth.controllers.impl;

import com.auth.controllers.interfaces.AuthControllerDocs;
import com.auth.dto.JwtAuthenticationResponse;
import com.auth.dto.SignInRequest;
import com.auth.dto.SignUpRequest;
import com.auth.services.AuthenticationService;
import com.auth.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthControllerDocs {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Override
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        try {
            return authenticationService.signUp(request);
        } catch (Exception e) {
            throw new RuntimeException("Error during sign up", e);
        }
    }

    @Override
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        try {
            return authenticationService.signIn(request);
        } catch (Exception e) {
            throw new RuntimeException("Error during sign in", e);
        }
    }

    @Override
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                if (jwtService.isTokenValidOnlyJwt(jwt)) {
                    return ResponseEntity.ok("OK");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating token");
        }
    }
}
