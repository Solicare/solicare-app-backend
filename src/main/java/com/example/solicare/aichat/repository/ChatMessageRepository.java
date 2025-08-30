package com.example.solicare.aichat.repository;

import com.example.solicare.aichat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 세션 ID를 기준으로 모든 메시지를 생성 시간 오름차순으로 조회
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    // 세션 ID에 해당하는 모든 메시지를 삭제
    @Transactional
    void deleteBySessionId(String sessionId);
}