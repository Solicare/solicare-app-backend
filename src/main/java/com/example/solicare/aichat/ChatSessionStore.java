package com.example.solicare.aichat;

import com.example.solicare.aichat.dto.ChatMessageDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionStore {

    private static final int MAX_MESSAGES = 40;           // 메시지 많아지면 비용 커짐 → 최근 N개만 유지
    private static final long TTL_SECONDS = 60L * 60L;    // 1시간 미사용 세션 자동 만료
    private final Map<String, Session> store = new ConcurrentHashMap<>();

    public synchronized List<ChatMessageDto> append(String sessionId, ChatMessageDto... newMsgs) {
        var session = store.getOrDefault(sessionId, new Session(new ArrayList<>(), Instant.now()));
        var list = new ArrayList<>(session.messages());
        Collections.addAll(list, newMsgs);

        if (list.size() > MAX_MESSAGES) {
            list = new ArrayList<>(list.subList(list.size() - MAX_MESSAGES, list.size()));
        }
        var updated = new Session(List.copyOf(list), Instant.now());
        store.put(sessionId, updated);
        return updated.messages();
    }

    public List<ChatMessageDto> get(String sessionId) {
        var s = store.get(sessionId);
        if (s == null) return List.of();
        if (Instant.now().getEpochSecond() - s.updatedAt().getEpochSecond() > TTL_SECONDS) {
            store.remove(sessionId);
            return List.of();
        }
        return s.messages();
    }

    public void reset(String sessionId) {
        store.remove(sessionId);
    }

    private record Session(List<ChatMessageDto> messages, Instant updatedAt) {
    }
}
