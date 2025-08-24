package com.example.solicare.aichat.controller;


import com.example.solicare.aichat.ChatSessionStore;
import com.example.solicare.aichat.dto.ChatMessageDto;
import com.example.solicare.aichat.dto.ChatRequestDto;
import com.example.solicare.aichat.dto.ChatResponseDto;
import com.example.solicare.aichat.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assistant/chat")
public class AiChatController {

    private final AiService aiService;
    private final ChatSessionStore sessionStore;

    /**
     * 유저 → AI (한 턴 보내고 응답 받기)
     */
    @PostMapping("/")
    public Mono<ChatResponseDto> chat(@RequestBody @Valid ChatRequestDto req) {
        // 1) 기존 세션 히스토리
        var history = new ArrayList<>(sessionStore.get(req.sessionId()));

        // 2) (선택) 클라가 보내준 extraHistory 합치기
        if (req.extraHistory() != null && !req.extraHistory().isEmpty()) {
            history.addAll(req.extraHistory());
        }

        // 3) 이번 user 메시지 추가
        var userMsg = new ChatMessageDto("user", req.userMessage());
        history = new ArrayList<>(sessionStore.append(req.sessionId(), userMsg));

        // 4) OpenAI payload 변환
        List<Map<String, String>> payloadMessages = history.stream()
                .map(m -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("role", m.role());
                    map.put("content", m.content());
                    return map;
                })
                .toList();

        // 5) 호출 → assistant 응답 저장 → 응답 반환
        return aiService.askChatGPTWithHistory(payloadMessages)
                .map(answer -> {
                    var assistant = new ChatMessageDto("assistant", answer);
                    var newHistory = sessionStore.append(req.sessionId(), assistant);
                    return new ChatResponseDto(req.sessionId(), newHistory, answer);
                });
    }

    /**
     * 세션 히스토리 조회
     */
    @GetMapping("/{sessionId}/history")
    public ChatResponseDto history(@PathVariable String sessionId) {
        var hist = sessionStore.get(sessionId);
        return new ChatResponseDto(sessionId, hist, null);
    }

    /**
     * 세션 리셋
     */
    @DeleteMapping("/{sessionId}")
    public void reset(@PathVariable String sessionId) {
        sessionStore.reset(sessionId);
    }
}

