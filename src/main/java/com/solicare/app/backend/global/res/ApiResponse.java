package com.solicare.app.backend.global.res;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// @Schema(name = "ApiResponse", description = "Standard API Response Wrapper")
@JsonPropertyOrder({"isSuccess", "code", "message", "body", "errors"})
public record ApiResponse<T>(
        @Schema(description = "API 요청 성공 여부") Boolean isSuccess,
        @Schema(description = "응답 코드") String code,
        @Schema(description = "응답 메시지") String message,
        T body,
        @Schema(description = "에러 메시지 목록") List<String> errors) {}
