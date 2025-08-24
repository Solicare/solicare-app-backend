package com.solicare.app.backend.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class MemberRequestDTO {
    public record Join(
            @Schema(
                            description = "회원 이름",
                            example = "홍길동",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이름은 필수입니다.")
                    String name,
            @Schema(
                            description = "전화번호",
                            example = "010-1234-5678",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "전화번호는 필수입니다.")
                    String phoneNumber,
            @Schema(
                            description = "이메일",
                            example = "address@test.com",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이메일은 필수입니다.")
                    String email,
            @Schema(
                            description = "비밀번호",
                            example = "securePassword123!",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "비밀번호는 필수입니다.")
                    String password) {}

    @Schema(name = "MemberRequestLogin", description = "로그인 요청 DTO")
    public record Login(
            @Schema(
                            description = "이메일",
                            example = "address@test.com",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이메일는 필수입니다.")
                    String email,
            @Schema(
                            description = "비밀번호",
                            example = "securePassword123!",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "비밀번호는 필수입니다.")
                    String password) {}
}
