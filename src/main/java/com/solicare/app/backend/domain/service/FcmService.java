package com.solicare.app.backend.domain.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.solicare.app.backend.domain.dto.output.push.FcmSendOutputDetail;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmService {
    private final FirebaseMessaging firebaseMessaging;

    public FcmSendOutputDetail sendMessageTo(String fcmToken, String title, String body) {
        Message message =
                Message.builder()
                        .setToken(fcmToken)
                        .setNotification(
                                Notification.builder().setTitle(title).setBody(body).build())
                        .build();

        try {
            firebaseMessaging.send(message);
            return FcmSendOutputDetail.of(FcmSendOutputDetail.Status.SENT, null);
        } catch (Exception e) {
            return FcmSendOutputDetail.of(FcmSendOutputDetail.Status.ERROR, e);
        }
    }
}
