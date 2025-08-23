package com.solicare.app.backend.application.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원정보 응답 DTO")
public record MemberProfileResponseDTO(
        String name,
        String email,
        String phoneNumber
) {
}
