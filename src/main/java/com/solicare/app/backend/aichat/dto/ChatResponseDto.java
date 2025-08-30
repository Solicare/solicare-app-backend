package com.solicare.app.backend.aichat.dto;

import java.util.List;

public record ChatResponseDto(
        String sessionId,
        List<ChatMessageDto> history, // 서버가 유지 중인 전체 히스토리(요약 X)
        String answer // 방금 받은 응답만 따로
        ) {}
