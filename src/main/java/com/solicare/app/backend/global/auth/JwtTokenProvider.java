package com.solicare.app.backend.global.auth;

import com.solicare.app.backend.domain.dto.output.auth.JwtValidateOutput;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final SecretKey SIGNING_KEY;
    private final int expirationMinutes;

    public JwtTokenProvider(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.expiration}") int expirationMinutes) {
        // Base64 디코딩 후 HMAC 키 생성 (AuthFilter와 동일)
        this.SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expirationMinutes = expirationMinutes;
    }

    public String createToken(String email, String userUuid) {
        Claims claims = Jwts.claims();
        claims.setSubject(email);
        claims.put("uuid", userUuid);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // payload
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMinutes * 60 * 1000L))
                .signWith(SIGNING_KEY) // 키에서 알고리즘 자동 선택
                .compact();
    }

    public JwtValidateOutput validateToken(String token) {
        try {
            Claims claims = parseToken(token);

            if (claims.getExpiration().before(new Date()))
                return JwtValidateOutput.of(JwtValidateOutput.Status.EXPIRED, null, null);
            return JwtValidateOutput.of(JwtValidateOutput.Status.VALID, claims, null);
        } catch (Exception e) {
            return JwtValidateOutput.of(JwtValidateOutput.Status.INVALID, null, e);
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
