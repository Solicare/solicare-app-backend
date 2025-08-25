package com.solicare.app.backend.global.auth;

import com.solicare.app.backend.domain.dto.output.auth.JwtValidateOutput;
import com.solicare.app.backend.domain.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

@Service
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

    public String createToken(List<Role> roles, String uuid) {
        Claims claims = Jwts.claims();
        claims.setSubject(uuid);
        claims.put("role", roles.stream().map(Role::name).toList());

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
            Jws<Claims> jwsClaims = parseToken(token);

            if (jwsClaims.getBody().getExpiration().before(new Date()))
                return JwtValidateOutput.of(JwtValidateOutput.Status.EXPIRED, null, null);
            return JwtValidateOutput.of(JwtValidateOutput.Status.VALID, jwsClaims, null);
        } catch (Exception e) {
            return JwtValidateOutput.of(JwtValidateOutput.Status.INVALID, null, e);
        }
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);
    }
}
