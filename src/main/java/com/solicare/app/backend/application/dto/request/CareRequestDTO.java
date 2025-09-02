package com.solicare.app.backend.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public class CareRequestDTO {
    @Schema(name = "MemberRequestLinkSenior", description = "모니터링 대상 연결 요청 DTO")
    public record LinkSenior(
            @Schema(
                            description = "모니터링 대상의 등록 ID",
                            example = "senioruser",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "모니터링 대상의 등록 ID는 필수입니다.")
                    String userId,
            @Schema(
                            description = "모니터링 대상의 등록 PW",
                            example = "password1234",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "모니터링 대상의 등록 PW는 필수입니다.")
                    String password) {}

    @Schema(name = "SeniorRequestLinkMember", description = "모니터링 보호자 연결 요청 DTO")
    public record LinkMember(
            @Schema(
                            description = "모니터링 보호자 이메일",
                            example = "address@test.com",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "모니터링 보호자의 이메일은 필수입니다.")
                    String email,
            @Schema(
                            description = "모니터링 보호자 PW",
                            example = "securePassword123!",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "모니터링 보호자의 PW는 필수입니다.")
                    String password) {}
}
