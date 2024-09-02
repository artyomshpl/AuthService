package com.auth.services;

import com.auth.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "jwtSigninKey", "kQa0bjHb0xPLuNxGLUiTb0ZnObwGN8dgbw7d7E2n67g=");
    }

    @Test
    public void testGenerateToken() {
        User user = new User("testUser", "password", "USER");
        String token = jwtService.generateToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testUser", claims.getSubject());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    public void testIsTokenValid() {
        User user = new User("testUser", "password", "USER");
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    private Key getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode("kQa0bjHb0xPLuNxGLUiTb0ZnObwGN8dgbw7d7E2n67g=");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}