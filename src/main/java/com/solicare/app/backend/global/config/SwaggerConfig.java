package com.solicare.app.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SwaggerConfig {
    @Bean
    public OpenAPI solicareAPI() {
        Info info =
                new Info()
                        .title("Solicare API")
                        .description("Solicare 서버의 API 명세서입니다.")
                        .version("1.0.0");

        String jwtSchemeName = "BearerAuth";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components =
                new Components()
                        .addSecuritySchemes(
                                jwtSchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description(
                                                "Enter your JWT token (without the Bearer prefix)"));

        return new OpenAPI()
                .openapi("3.0.1")
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components)
                .addServersItem(
                        new Server()
                                .url("https://api.solicare.kro.kr")
                                .description("Production server"))
                .addServersItem(
                        new Server()
                                .url("https://dev-api.solicare.kro.kr")
                                .description("Development server"))
                .addServersItem(
                        new Server()
                                .url(
                                        String.format(
                                                "http://localhost:%s",
                                                Optional.ofNullable(System.getenv("server.port"))
                                                        .orElse("8080")))
                                .description("Local server"));
    }
}
