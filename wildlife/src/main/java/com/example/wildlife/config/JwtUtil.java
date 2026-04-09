package com.example.wildlife.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * HS256 JWT helper. Secret must be at least 256 bits (32 ASCII chars) for HMAC-SHA256.
 */
@Component
public class JwtUtil {

    public static final String CLAIM_USER_ID = "uid";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    private SecretKey signingKey() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret is not set in application.properties");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "jwt.secret must be at least 32 characters (256 bits) for HS256");
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    /** Issue a token after successful login. */
    public String generateToken(String email, Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_USER_ID, userId)
                .issuedAt(now)
                .expiration(exp)
                .signWith(signingKey())
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Object v = parseClaims(token).get(CLAIM_USER_ID);
        if (v instanceof Number n) {
            return n.longValue();
        }
        if (v != null) {
            return Long.valueOf(v.toString());
        }
        return null;
    }

    /** Returns false for expired, bad signature, or malformed tokens. */
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
