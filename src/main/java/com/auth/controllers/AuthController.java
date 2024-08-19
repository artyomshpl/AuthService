package com.auth.controllers;

import com.auth.dto.JwtAuthenticationResponse;
import com.auth.dto.SignInRequest;
import com.auth.dto.SignUpRequest;
import com.auth.services.AuthenticationService;
import com.auth.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Operation(summary = "Sign up a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sign up",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Sign in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sign in",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Validate a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (jwtService.isTokenValidOnlyJwt(jwt)) {
                return ResponseEntity.ok("OK");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
}
