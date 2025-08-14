package com.example.solicare.domain.member.application.dto;

import com.example.solicare.domain.member.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 요청 DTO")
public class MemberSaveRequestDTO {

    @Schema(description = "회원 이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Schema(description = "보호 대상자 이름", example = "홍순자", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "어르신 이름은 필수입니다.")
    private String elderlyName;

    @Schema(description = "전화번호", example = "01012345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @Schema(description = "비밀번호", example = "securePassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"})
    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @Schema(description = "주소", example = "서울특별시 동작구 흑석로 84")
    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @Schema(description = "나이", example = "32")
    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150 이하이어야 합니다.")

    private Integer age;

    @Schema(description = "특이사항", example = "고혈압 주의")
    private String specialNote;
}
