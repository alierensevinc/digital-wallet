package com.alierensevinc.dwallet.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY_STRING = "VGhpcy1pcy1hLXNlY3JldC1rZXktZm9yLUpXVC1HZW5lcmF0aW9uIQ=="; // Replace with a securely generated Base64 key

    // Convert the Base64-encoded key into a valid HMAC-SHA256 key
    private final Key SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY_STRING), SignatureAlgorithm.HS256.getJcaName());

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token valid for 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // ✅ Fixed for JJWT 0.9.1
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // ✅ Fixed for JJWT 0.9.1
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}