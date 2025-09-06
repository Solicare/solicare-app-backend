package com.solicare.app.backend.application.dto.request;

import com.solicare.app.backend.application.enums.PushChannel;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PushRequestDTO {
    @Schema(name = "PushRequestSend", description = "푸시 메시지 발송 요청 DTO")
    public record Send(
            @Schema(
                            description = "푸시 채널",
                            example = "INFO",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "채널은 필수입니다.")
                    PushChannel channel,
            @Schema(
                            description = "알림 제목",
                            example = "테스트 알림",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "제목은 필수입니다.")
                    String title,
            @Schema(
                            description = "알림 내용",
                            example = "테스트 메시지입니다.",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "내용은 필수입니다.")
                    String message) {}

    @Schema(name = "PushRequestRegisterToken", description = "푸시 토큰 갱신 요청 DTO")
    public record RenewToken(
            @Schema(
                            description = "기존 토큰",
                            example = "old_token",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "기존 토큰은 필수입니다.")
                    String oldToken,
            @Schema(
                            description = "새 토큰",
                            example = "new_token",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "새 토큰은 필수입니다.")
                    String newToken) {}
}
