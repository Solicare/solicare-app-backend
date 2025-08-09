package com.example.solicare.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey SIGNING_KEY;
    private final int expirationMinutes;

    public JwtTokenProvider(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.expiration}") int expirationMinutes
    ) {
        // Base64 디코딩 후 HMAC 키 생성 (AuthFilter와 동일)
        this.SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expirationMinutes = expirationMinutes;
    }

    // subject에는 현재 phoneNumber를 넣어 사용
    public String createToken(String subject, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(createClaims(subject, role))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMinutes * 60 * 1000L))
                .signWith(SIGNING_KEY) // 키에서 알고리즘 자동 선택
                .compact();
    }

    private Claims createClaims(String subject, String role) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("role", role);
        return claims;
    }
}
