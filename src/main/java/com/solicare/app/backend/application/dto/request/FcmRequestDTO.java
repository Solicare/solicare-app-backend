package com.solicare.app.backend.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class FcmRequestDTO {
    public record Send(
            @Schema(
                            description = "디바이스 토큰",
                            example = "fcm_device_token_here",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "토큰은 필수입니다.")
                    String token,
            @Schema(
                            description = "알림 제목",
                            example = "긴급 알림",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "제목은 필수입니다.")
                    String title,
            @Schema(
                            description = "알림 내용",
                            example = "위급 상황이 감지되었습니다.",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "내용은 필수입니다.")
                    String body) {}

    public record Register(
            @Schema(
                            description = "디바이스 토큰",
                            example = "fcm_device_token_here",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "토큰은 필수입니다.")
                    String token,
            @Schema(
                            description = "로컬 디바이스 번호",
                            example = "010-1234-5678",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "")
                    String localDeviceNumber) {}

    public record Delete(
            @Schema(
                            description = "디바이스 토큰",
                            example = "fcm_device_token_here",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "토큰은 필수입니다.")
                    String token) {}
}
