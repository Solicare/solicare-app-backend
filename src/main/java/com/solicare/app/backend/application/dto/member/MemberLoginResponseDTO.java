package com.solicare.app.backend.application.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 DTO")
public record MemberLoginResponseDTO(
        String name,
        String token
) {
}
