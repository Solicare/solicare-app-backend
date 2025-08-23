package com.example.solicare.application.dto.senior;

import com.example.solicare.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모니터링 대상 정보 응답 DTO")
public record SeniorProfileResponseDTO(
        String monitorUserId,
        String name,
        Integer age,
        Gender gender,
        String phoneNumber,
        String address,
        String note
) {
}
