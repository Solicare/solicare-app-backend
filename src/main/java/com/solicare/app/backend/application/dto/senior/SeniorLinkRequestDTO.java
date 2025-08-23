package com.solicare.app.backend.application.dto.senior;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "모니터링 대상 연결 요청 DTO")
public class SeniorLinkRequestDTO {
    @Schema(description = "연결 대상의 회원 UUID", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "연결 대상의 회원 UUID는 필수입니다.")
    private String monitorUserUuid;

    @Schema(description = "모니터링 대상의 등록 ID", example = "testuser", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "모니터링 대상의 등록 ID는 필수입니다.")
    private String userId;

    @Schema(description = "모니터링 대상의 등록 PW", example = "testpassword", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "모니터링 대상의 등록 PW는 필수입니다.")
    private String password;
}
