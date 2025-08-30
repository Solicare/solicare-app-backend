package com.solicare.app.backend.aichat.service;

import com.solicare.app.backend.aichat.dto.ChatMessageDto;
import com.solicare.app.backend.aichat.dto.ChatRequestDto;
import com.solicare.app.backend.aichat.dto.ChatResponseDto;
import com.solicare.app.backend.aichat.entity.ChatMessage;
import com.solicare.app.backend.aichat.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private static final int MAX_HISTORY_SIZE = 40; // DB에서 조회할 최근 대화 개수
    private final AiService aiService; // OpenAI 호출 서비스
    private final ChatMessageRepository chatMessageRepository; // DB 저장소

    @Transactional
    public Mono<ChatResponseDto> chat(ChatRequestDto req) {
        // 1. DB에서 기존 대화 내역 조회
        List<ChatMessage> dbHistory =
                chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(req.sessionId());

        // 만약 대화가 너무 길면 최근 N개만 사용 (비용 및 성능 최적화)
        if (dbHistory.size() > MAX_HISTORY_SIZE) {
            dbHistory = dbHistory.subList(dbHistory.size() - MAX_HISTORY_SIZE, dbHistory.size());
        }

        List<ChatMessageDto> history =
                dbHistory.stream()
                        .map(m -> new ChatMessageDto(m.getRole(), m.getContent()))
                        .collect(Collectors.toCollection(ArrayList::new));

        // 2. (선택) 클라이언트가 보낸 추가 히스토리 합치기
        if (req.extraHistory() != null && !req.extraHistory().isEmpty()) {
            history.addAll(req.extraHistory());
        }

        // 3. 이번 사용자 메시지를 DB에 저장하고, 히스토리에 추가
        saveMessage(req.sessionId(), "user", req.userMessage());
        history.add(new ChatMessageDto("user", req.userMessage()));

        // 4. OpenAI payload 생성
        List<Map<String, String>> payloadMessages =
                history.stream()
                        .map(m -> Map.of("role", m.role(), "content", m.content()))
                        .toList();

        // 5. OpenAI API 호출
        return aiService
                .askChatGPTWithHistory(payloadMessages)
                .flatMap(
                        answer -> {
                            // 6. AI 응답을 DB에 저장
                            saveMessage(req.sessionId(), "assistant", answer);

                            // 7. 최종 응답 객체 생성
                            List<ChatMessageDto> finalHistory = getHistoryDto(req.sessionId());
                            return Mono.just(
                                    new ChatResponseDto(req.sessionId(), finalHistory, answer));
                        });
    }

    public List<ChatMessageDto> getHistoryDto(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(m -> new ChatMessageDto(m.getRole(), m.getContent()))
                .toList();
    }

    private void saveMessage(String sessionId, String role, String content) {
        ChatMessage message =
                ChatMessage.builder().sessionId(sessionId).role(role).content(content).build();
        chatMessageRepository.save(message);
    }

    public ChatResponseDto getHistory(String sessionId) {
        List<ChatMessageDto> history = getHistoryDto(sessionId);
        return new ChatResponseDto(sessionId, history, null);
    }

    public void resetHistory(String sessionId) {
        chatMessageRepository.deleteBySessionId(sessionId);
    }
}
