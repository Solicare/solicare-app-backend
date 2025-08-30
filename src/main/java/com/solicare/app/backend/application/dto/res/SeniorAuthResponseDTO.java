package com.solicare.app.backend.application.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeniorAuthResponseDTO {

    @Schema(name = "SeniorAuthJoinResponse", description = "모니터링 대상 회원가입 응답 DTO")
    public record Join(@Schema(description = "JWT 토큰") String token) {}

    @Schema(name = "SeniorAuthLoginResponse", description = "모니터링 대상 로그인 응답 DTO")
    public record Login(
            @Schema(description = "로그인된 사용자 이름") String name,
            @Schema(description = "JWT 토큰") String token
    ) {}
}