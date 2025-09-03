package com.solicare.app.backend.domain.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.solicare.app.backend.domain.dto.output.push.SendOutputDetail;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FirebaseService {
    private final FirebaseMessaging firebaseMessaging;

    public SendOutputDetail sendMessageTo(String fcmToken, String title, String body) {
        Message message =
                Message.builder()
                        .setToken(fcmToken)
                        .setNotification(
                                Notification.builder().setTitle(title).setBody(body).build())
                        .build();

        try {
            firebaseMessaging.send(message);
            return SendOutputDetail.of(SendOutputDetail.Status.SENT, null);
        } catch (Exception e) {
            if (e instanceof FirebaseMessagingException fcmEx) {
                return SendOutputDetail.of(
                        SendOutputDetail.Status.ERROR,
                        new RuntimeException(
                                "FCM Error: " + fcmEx.getErrorCode() + " - " + fcmEx.getMessage()));
            } else {
                return SendOutputDetail.of(
                        SendOutputDetail.Status.ERROR, new RuntimeException(e.getMessage()));
            }
        }
    }
}
