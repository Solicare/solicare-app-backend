package com.solicare.app.backend.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

// TODO: refactor doFilterInternal() - extract methods, separate steps
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey SIGNING_KEY;

    public JwtAuthFilter(@Value("${jwt.secretKey}") String secretKey) {
        // Base64 디코딩 후 HMAC 키 생성 (TokenProvider와 동일 방식)
        this.SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // 1) Authorization 헤더 없거나 "Bearer " 아님 → 통과
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 2) 토큰 추출
        String token = header.substring(7).trim();
        if (token.isEmpty()) { // "Bearer "만 온 경우
            chain.doFilter(request, response);
            return;
        }

        try {
            // 3) 토큰 파싱
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = String.valueOf(claims.get("role"));

            // TODO: use JwtAuthenticationToken instead of UsernamePasswordAuthenticationToken
            // 4) 인증 객체 생성 및 SecurityContext에 저장
            var authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), // subject = phoneNumber
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (JwtException e) {
            // 서명/만료/형식 오류 등 JWT 예외
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"invalid token\"}");
        }
    }
}
