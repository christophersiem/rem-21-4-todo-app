package de.neuefische.backend.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JWTUtilService {

    private static final long TOKEN_LIFETIME = 4L * 60L * 60L * 1000L;

    @Value("${neuefische.todo.jwt.secret}")
    private String jwtSecret;

    @Value("${neuefische.todo.jwt.tokenLifetime:14400000}")
    private long tokenLifetime;

    public String createToken(Map<String, Object> claims, String subject) {

        // Generate JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMillis(tokenLifetime))))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
