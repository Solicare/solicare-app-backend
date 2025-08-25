package com.solicare.app.backend.global.auth;

import com.solicare.app.backend.domain.dto.output.auth.JwtValidateOutput;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.domain.repository.SeniorRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        log.info(
                "🔍 JWT Filter - Request URI: {}, Authorization Header: {}",
                request.getRequestURI(),
                header != null
                        ? "Present (" + header.substring(0, Math.min(20, header.length())) + "...)"
                        : "null");

        // 1) No Authorization header
        // -> Filter pass(401 is handled by ExceptionTranslationFilter)
        if (header == null) {
            log.info("❌ No Authorization header, passing to next filter");
            chain.doFilter(request, response);
            return;
        }

        // 2) Not Bearer
        if (!header.startsWith("Bearer ")) {
            log.info("❌ Authorization header does not start with Bearer: {}", header);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            // TODO: modify to respond via ApiResponse<T>
            response.getWriter()
                    .write(
                            "{\"message\":\"Authentication Failed.\", \"reason\":\"Authorization Header is not Bearer format\"}");
            return;
        }

        // 3) Extract JWT token
        // Empty -> Filter pass(401 is handled by ExceptionTranslationFilter)
        String token = header.substring(7).trim();
        log.info("🔑 Extracted token length: {}", token.length());

        if (token.isEmpty()) {
            log.info("❌ Token is empty, passing to next filter");
            chain.doFilter(request, response);
            return;
        }

        // 4) Validate JWT token
        JwtValidateOutput output = jwtTokenProvider.validateToken(token);
        log.info("✅ Token validation status: {}", output.getStatus());

        if (output.getStatus() != JwtValidateOutput.Status.VALID) {
            log.info("❌ Token validation failed: {}", output.getStatus());
            sendJwtValidateErrorResponse(response, output);
            return;
        }

        // 5) Extract roles from claims
        Jws<Claims> jwsClaims = output.getJwsClaims();
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        log.info("👤 Subject: {}", subject);
        List<String> roles =
                Optional.ofNullable(claims.get("role"))
                        .filter(List.class::isInstance)
                        .map(List.class::cast)
                        .map(list -> (List<?>) list)
                        .map(list -> list.stream().map(Object::toString).toList())
                        .orElseGet(List::of);

        log.info("🔑 Roles from token: {}", roles);

        if (roles.isEmpty()) {
            log.info("❌ Role claim is missing in token");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            // TODO: modify to respond via ApiResponse<T>
            response.getWriter()
                    .write(
                            "{\"message\":\"Authentication Failed.\", \"reason\":\"Role claim is missing in token.\"}");
            return;
        }

        // 5) Validate roles with DB and create Authority list

        // 6) Create Authentication and set in SecurityContext (no password needed for JWT)
        List<GrantedAuthority> authorities = buildAuthoritiesFromRoles(claims.getSubject(), roles);
        log.info(
                "🛡️ Built authorities: {}",
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(java.util.stream.Collectors.toList()));

        if (authorities.isEmpty()) {
            log.info("❌ No valid authorities found for user");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter()
                    .write(
                            "{\"message\":\"Authentication Failed.\", \"reason\":\"No valid authorities found for user.\"}");
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(
                "✅ Authentication successful: {} with authorities: {}",
                authentication.getPrincipal(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(java.util.stream.Collectors.toList()));
        chain.doFilter(request, response);
    }

    @NonNull
    private List<GrantedAuthority> buildAuthoritiesFromRoles(
            String subject, List<String> roleNames) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roleNames) {
            if (validateRoleWithSubject(Role.valueOf(role), subject)) {
                authorities.add(() -> "ROLE_" + role);
            }
        }
        return authorities;
    }

    private boolean validateRoleWithSubject(Role role, String subject) {
        switch (role) {
            case MEMBER -> {
                return memberRepository.existsByUuid(subject);
            }
            case SENIOR -> {
                return seniorRepository.existsByUserId(subject);
            }
        }
        return false;
    }

    // TODO: modify to respond via ApiResponse<T>
    private void sendJwtValidateErrorResponse(
            HttpServletResponse response, JwtValidateOutput output) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        String reason;
        switch (output.getStatus()) {
            case JwtValidateOutput.Status.INVALID -> {
                reason = "invalid token";
                Exception e = output.getException();
                if (e != null) {
                    reason += ": " + e.getMessage();
                }
            }
            case JwtValidateOutput.Status.EXPIRED -> reason = "expired token";
            default -> throw new RuntimeException("not catched status: " + output.getStatus());
        }
        response.getWriter()
                .write("{\"message\":\"Authentication Failed.\", \"reason\":\"" + reason + "\"}");
    }
}
