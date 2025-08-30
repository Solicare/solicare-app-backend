package com.solicare.app.backend.application.dto.request;

import com.solicare.app.backend.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeniorAuthRequestDTO {

    @Schema(name = "SeniorAuthRequestJoin", description = "모니터링 대상 회원가입 요청 DTO")
    public record Join(
            @Schema(description = "사용자 ID", example = "senioruser", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "사용자 ID는 필수입니다.")
            String userId,

            @Schema(description = "비밀번호", example = "password1234", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "비밀번호는 필수입니다.")
            String password,

            @Schema(description = "이름", example = "김어르신", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "이름은 필수입니다.")
            String name,

            @Schema(description = "나이", example = "82", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "나이는 필수입니다.")
            @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
            @Max(value = 120, message = "나이는 120 이하이어야 합니다.")
            Integer age,

            @Schema(description = "성별", example = "FEMALE", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "성별은 필수입니다.")
            Gender gender,

            @Schema(description = "전화번호", example = "010-9876-5432")
            String phoneNumber,

            @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 212", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "주소는 필수입니다.")
            String address,

            @Schema(description = "특이사항", example = "주 3회 산책 필요")
            String note
    ) {}

    @Schema(name = "SeniorAuthRequestLogin", description = "모니터링 대상 로그인 요청 DTO")
    public record Login(
            @Schema(description = "사용자 ID", example = "senioruser", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "사용자 ID는 필수입니다.")
            String userId,

            @Schema(description = "비밀번호", example = "password1234", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "비밀번호는 필수입니다.")
            String password
    ) {}
}