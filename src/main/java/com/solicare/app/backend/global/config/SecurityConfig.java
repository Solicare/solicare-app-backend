package com.solicare.app.backend.global.config;

import com.solicare.app.backend.global.auth.JwtAuthFilter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

@Configuration
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

                                                    response.setStatus(401);
                                                    response.setContentType("application/json");

                                                    // TODO: modify to respond via ApiResponse<T>
                                                    response.getWriter()
                                                            .write(
                                                                    "{\"message\":\"Authentification needed.\", \"reason\":\"Authorization Header is required\"}");
                                                })
                                        .accessDeniedHandler(
                                                (request, response, accessDeniedException) -> {
                                                    logSecurityException(
                                                            "🔒 AccessDeniedHandler",
                                                            request,
                                                            accessDeniedException);

                                                    response.setStatus(403);
                                                    response.setContentType("application/json");

                                                    // TODO: modify to respond via ApiResponse<T>
                                                    response.getWriter()
                                                            .write(
                                                                    "{\"message\":\"Access denied.\", \"reason\":\"You do not have permission to access this resource.\"}");
                                                }))
                // Use JwtAuthFilter instead of UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private void logSecurityException(
            String handlerName,
            jakarta.servlet.http.HttpServletRequest request,
            Exception exception) {
        log.error("{} triggered!", handlerName);
        log.error("Request URI: {}", request.getRequestURI());
        log.error("Request Method: {}", request.getMethod());
        log.error("Authorization Header: {}", request.getHeader("Authorization"));
        log.error("Exception: {}", exception.getClass().getSimpleName());
        log.error("Exception Message: {}", exception.getMessage());
        log.error("Exception Stack Trace: ", exception);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(
                Arrays.asList(
                        "http://localhost",
                        "http://localhost:*",
                        "https://*.solicare.kro.kr")); // localhost와 *.solicare.kro.kr 허용
        configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 허용
        configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 Header 허용
        configuration.setAllowCredentials(true); // 인증 정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder makePassword() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
