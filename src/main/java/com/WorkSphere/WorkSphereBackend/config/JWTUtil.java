package com.WorkSphere.WorkSphereBackend.config;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.WorkSphere.WorkSphereBackend.enums.Role;

import javax.crypto.SecretKey;
import java.util.Date;
 
@Component
public class JWTUtil {
 
    private String secret = "mySuperSecretKeyForJWTAuthentication123456";
 
    private long expiration =  1000 * 60 * 60;
 
    // 🔐 Generate signing key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
 
    // ✅ Generate JWT token
    public String generateToken(String email, Role role) {
 
        return Jwts.builder()
                .subject(email)                      // identity
                .claim("role", role)                 // custom claim
                .issuedAt(new Date())                // created time
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
 
    // ✅ Extract Email
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
 
    // ✅ Extract Role
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
 
    // ✅ Validate Token
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
 
    // 🔍 Extract all claims
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}