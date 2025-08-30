package com.solicare.app.backend.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solicare.app.backend.domain.dto.output.auth.JwtValidateOutput;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.domain.repository.SeniorRepository;
import com.solicare.app.backend.global.apiPayload.ApiResponse;
import com.solicare.app.backend.global.apiPayload.response.status.GlobalStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    // 💥 ObjectMapper를 주입받아 ApiResponse를 JSON으로 변환하는 데 사용
    private final ObjectMapper objectMapper;

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

        if (header == null) {
            log.info("❌ No Authorization header, passing to next filter");
            chain.doFilter(request, response);
            return;
        }

        if (!header.startsWith("Bearer ")) {
            log.info("❌ Authorization header does not start with Bearer: {}", header);
            // 💥 수정된 부분: ApiResponse 형식으로 응답
            sendErrorResponse(response, "Authorization Header is not Bearer format");
            return;
        }

        String token = header.substring(7).trim();
        log.info("🔑 Extracted token length: {}", token.length());

        if (token.isEmpty()) {
            log.info("❌ Token is empty, passing to next filter");
            chain.doFilter(request, response);
            return;
        }

        JwtValidateOutput output = jwtTokenProvider.validateToken(token);
        log.info("✅ Token validation status: {}", output.getStatus());

        if (output.getStatus() != JwtValidateOutput.Status.VALID) {
            log.info("❌ Token validation failed: {}", output.getStatus());
            sendJwtValidateErrorResponse(response, output);
            return;
        }

        Jws<Claims> jwsClaims = output.getJwsClaims();
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        log.info("👤 Subject: {}", subject);
        List<String> roles =
                Optional.ofNullable(claims.get("role"))
                        .filter(List.class::isInstance)
                        .map(list -> (List<?>) list)
                        .map(list -> list.stream().map(Object::toString).toList())
                        .orElseGet(List::of);

        log.info("🔑 Roles from token: {}", roles);

        if (roles.isEmpty()) {
            log.info("❌ Role claim is missing in token");
            // 💥 수정된 부분: ApiResponse 형식으로 응답
            sendErrorResponse(response, "Role claim is missing in token.");
            return;
        }

        List<GrantedAuthority> authorities = buildAuthoritiesFromRoles(claims.getSubject(), roles);
        log.info(
                "🛡️ Built authorities: {}",
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        if (authorities.isEmpty()) {
            log.info("❌ No valid authorities found for user");
            // 💥 수정된 부분: ApiResponse 형식으로 응답
            sendErrorResponse(response, "No valid authorities found for user.");
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
                        .collect(Collectors.toList()));
        chain.doFilter(request, response);
    }

    private void sendJwtValidateErrorResponse(
            HttpServletResponse response, JwtValidateOutput output) throws IOException {
        String reason;
        switch (output.getStatus()) {
            case JwtValidateOutput.Status.INVALID -> {
                reason = "Invalid token";
                Exception e = output.getException();
                if (e != null) {
                    reason += ": " + e.getMessage();
                }
            }
            case JwtValidateOutput.Status.EXPIRED -> reason = "Expired token";
            default -> reason = "Unknown token error";
        }
        // 💥 수정된 부분: ApiResponse 형식으로 응답
        sendErrorResponse(response, reason);
    }

    /**
     * 💥 추가된 헬퍼 메서드:
     * Spring Security Filter 단에서 ApiResponse 형식의 에러 응답을 생성합니다.
     * @param response HttpServletResponse
     * @param message 응답 메시지에 포함될 동적 메시지
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        GlobalStatus errorStatus = GlobalStatus._UNAUTHORIZED;
        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.onFailure(errorStatus.getCode(), message, null);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(jsonResponse);
    }

    @NonNull
    private List<GrantedAuthority> buildAuthoritiesFromRoles(
            String subject, List<String> roleNames) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roleNames) {
            try {
                if (validateRoleWithSubject(Role.valueOf(role), subject)) {
                    authorities.add(() -> "ROLE_" + role);
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid role found in token: {}", role);
            }
        }
        return authorities;
    }

    private boolean validateRoleWithSubject(Role role, String subject) {
        return switch (role) {
            case MEMBER -> memberRepository.existsByUuid(subject);
            case SENIOR -> seniorRepository.existsByUserId(subject);
            // 'ADMIN'과 같은 다른 역할은 DB 검증 없이 통과시킬 수 있습니다.
            case ADMIN -> true;
        };
    }
}