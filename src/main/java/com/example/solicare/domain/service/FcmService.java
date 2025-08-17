package com.example.solicare.domain.service;

import com.example.solicare.domain.entity.DeviceToken;
import com.example.solicare.domain.entity.Member;
import com.example.solicare.domain.repository.DeviceTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final DeviceTokenRepository deviceTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    public boolean sendMessageTo(Member member, String title, String body) {
        Long memberId = member.getId();
        Optional<DeviceToken> deviceToken = deviceTokenRepository.findByMemberId(memberId);
        return deviceToken.filter(token -> sendMessageTo(token.getToken(), title, body)).isPresent();
    }

    public boolean sendMessageTo(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            firebaseMessaging.send(message);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to send FCM message", e);
        }
    }
}
