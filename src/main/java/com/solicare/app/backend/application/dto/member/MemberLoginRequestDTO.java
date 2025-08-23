package com.solicare.app.backend.application.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class MemberLoginRequestDTO {
    @Schema(description = "이메일", example = "address@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이메일는 필수입니다.")
    private String email;

    @Schema(description = "비밀번호", example = "securePassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
