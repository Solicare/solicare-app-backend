package com.solicare.app.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.domain.repository.SeniorRepository;
import com.solicare.app.backend.global.auth.JwtAuthFilter;
import com.solicare.app.backend.global.auth.JwtTokenProvider;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public JwtAuthFilter jwtAuthFilter(
            JwtTokenProvider jwtTokenProvider,
            MemberRepository memberRepository,
            SeniorRepository seniorRepository) {
        return JwtAuthFilter.of(jwtTokenProvider, memberRepository, seniorRepository, objectMapper);
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(
            HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                "/",
                                                "/favicon.ico",
                                                "/robots.txt",
                                                "/error",
                                                "/docs",
                                                "/webjars/**",
                                                "/actuator/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-resources/**",
                                                "/api/member/join",
                                                "/api/member/login",
                                                "/api/senior/join",
                                                "/api/senior/login",
                                                "/api/fcm/register/{token}",
                                                "/api/fcm/unregister/{token}")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .exceptionHandling(
                        eh ->
                                eh.authenticationEntryPoint(
                                                (request, response, authException) -> {
                                                    logSecurityException(
                                                            "🚨 AuthenticationEntryPoint",
                                                            request,
                                                            authException);

                                                    response.setStatus(
                                                            ApiStatus._UNAUTHORIZED
                                                                    .getHttpStatus()
                                                                    .value());
                                                    response.setContentType(
                                                            "application/json;charset=UTF-8");
                                                    response.getWriter()
                                                            .write(
                                                                    objectMapper.writeValueAsString(
                                                                            ApiResponse.of(
                                                                                    false,
                                                                                    ApiStatus
                                                                                            ._UNAUTHORIZED
                                                                                            .getCode(),
                                                                                    ApiStatus
                                                                                            ._UNAUTHORIZED
                                                                                            .getMessage(),
                                                                                    null)));
                                                })
                                        .accessDeniedHandler(
                                                (request, response, accessDeniedException) -> {
                                                    logSecurityException(
                                                            "🔒 AccessDeniedHandler",
                                                            request,
                                                            accessDeniedException);

                                                    response.setStatus(
                                                            ApiStatus._FORBIDDEN
                                                                    .getHttpStatus()
                                                                    .value());
                                                    response.setContentType(
                                                            "application/json;charset=UTF-8");
                                                    response.getWriter()
                                                            .write(
                                                                    objectMapper.writeValueAsString(
                                                                            ApiResponse.of(
                                                                                    false,
                                                                                    ApiStatus
                                                                                            ._FORBIDDEN
                                                                                            .getCode(),
                                                                                    ApiStatus
                                                                                            ._FORBIDDEN
                                                                                            .getMessage(),
                                                                                    null)));
                                                }))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private void logSecurityException(
            String handlerName,
            jakarta.servlet.http.HttpServletRequest request,
            Exception exception) {
        log.warn("{} triggered for request URI: {}", handlerName, request.getRequestURI());
        log.warn("Exception: {}", exception.getMessage());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(
                Arrays.asList(
                        "http://localhost", "http://localhost:*", "https://*.solicare.kro.kr"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder makePassword() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
