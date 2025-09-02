package com.solicare.app.backend.aichat.controller;

import com.solicare.app.backend.aichat.dto.ChatRequestDto;
import com.solicare.app.backend.aichat.dto.ChatResponseDto;
import com.solicare.app.backend.aichat.service.AiChatService; // 새로 만든 서비스 import

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assistant/chat")
public class AiChatController {

    private final AiChatService aiChatService; // 기존 AiService와 ChatSessionStore 대신 주입

    /** 유저 → AI (한 턴 보내고 응답 받기) */
    @PostMapping()
    public Mono<ChatResponseDto> chat(@RequestBody @Valid ChatRequestDto req) {
        return aiChatService.chat(req);
    }

    /** 세션 히스토리 조회 */
    @GetMapping("/{sessionId}/history")
    public ChatResponseDto history(@PathVariable String sessionId) {
        return aiChatService.getHistory(sessionId);
    }

    /** 세션 리셋 */
    @DeleteMapping("/{sessionId}")
    public void reset(@PathVariable String sessionId) {
        aiChatService.resetHistory(sessionId);
    }
}
