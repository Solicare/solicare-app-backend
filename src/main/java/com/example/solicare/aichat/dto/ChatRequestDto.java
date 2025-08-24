package com.example.solicare.aichat.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatRequestDto(
        @NotBlank String sessionId,          // 클라이언트가 생성/보관 (ex: UUID)
        @NotBlank String userMessage,        // 이번에 사용자가 보낸 메시지
        List<ChatMessageDto> extraHistory    // (선택) 추가 히스토리 합치고 싶으면
) {
}
