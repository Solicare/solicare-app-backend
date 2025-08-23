package com.example.solicare.application.dto.senior;

import com.example.solicare.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "모니터링 대상 연결 요청 DTO")
public class SeniorAddRequestDTO {
    @Schema(description = "연결 대상의 회원 UUID", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "연결 대상의 회원 UUID는 필수입니다.")
    private String monitorUserUuid;

    @Schema(description = "모니터링 대상의 등록 ID", example = "testuser", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "모니터링 대상의 등록 ID는 필수입니다.")
    private String userId;

    @Schema(description = "모니터링 대상의 등록 PW", example = "testpassword", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "모니터링 대상의 등록 PW는 필수입니다.")
    private String password;

    @Schema(description = "모니터링 대상 이름", example = "홍순자", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Schema(description = "모니터링 대상 성별", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @Schema(description = "모니터링 대상 나이", example = "82", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 120, message = "나이는 120 이하이어야 합니다.")
    private Integer age;

    @Schema(description = "모니터링 대상 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "모니터링 대상 주소", example = "서울특별시 동작구 흑석로 84", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @Schema(description = "특이사항", example = "고혈압 주의")
    private String note;

}
