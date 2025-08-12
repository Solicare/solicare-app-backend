package com.example.solicare.domain.member.application.dto;

import com.example.solicare.domain.member.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.*; // 컨트롤러에서 @Valid 사용할 경우

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class MemberSaveRequestDTO {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "어르신 이름은 필수입니다.")
    private String elderlyName;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;        // ★ 추가

    @NotBlank(message = "주소는 필수입니다.")
    private String address;       // ★ 추가

    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150 이하이어야 합니다.")
    private Integer age;          // ★ 추가

    // 특이사항은 선택값 (검증 X)
    private String specialNote;   // ★ 추가
}
