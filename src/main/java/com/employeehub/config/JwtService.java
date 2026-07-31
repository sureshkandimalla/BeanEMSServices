package com.employeehub.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// Issues and verifies the app's own session token — separate from the
// Google ID token, which is only verified once at login (see
// AuthController). Re-verifying with Google on every single API request
// would mean an outbound call to Google per request; instead login mints
// one of these, signed with a secret only this backend knows, and every
// later request just checks that signature locally.
@Component
public class JwtService {

    private static final long EXPIRY_MILLIS = 12L * 60 * 60 * 1000; // 12 hours

    private final SecretKey key;

    public JwtService(@Value("${app.jwt-secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String issueToken(String email, String tenant) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim("tenant", tenant)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRY_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Returns null (rather than throwing) on any invalid/expired/malformed
    // token — callers treat that uniformly as "not authenticated".
    public Claims verifyToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
