package com.auth.controllers.interfaces;

import com.auth.dataTransferObjects.JwtAuthenticationResponse;
import com.auth.dataTransferObjects.SignInRequest;
import com.auth.dataTransferObjects.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {

    @Operation(summary = "Sign up a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sign up",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/sign-up")
    JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request);

    @Operation(summary = "Sign in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sign in",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/sign-in")
    JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request);

    @Operation(summary = "Validate a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/validate-token")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);
}