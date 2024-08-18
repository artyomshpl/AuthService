package com.auth.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class UtilGenertator {
    public static void main(String[] args) {
        // Generate a new secret key
        byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);

// Print the secret key
        System.out.println("Secret key: " + secretKey);
    }
}
