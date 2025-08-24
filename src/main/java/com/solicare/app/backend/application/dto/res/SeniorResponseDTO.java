package com.solicare.app.backend.application.dto.res;

import com.solicare.app.backend.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeniorResponseDTO {
  @Schema(description = "모니터링 대상 정보 응답 DTO")
  public record Profile(
      @Schema(description = "사용자 ID") String userId,
      @Schema(description = "이름") String name,
      @Schema(description = "나이") Integer age,
      @Schema(description = "성별") Gender gender,
      @Schema(description = "전화번호") String phoneNumber,
      @Schema(description = "주소") String address,
      @Schema(description = "비고") String note) {}
}
