package com.example.solicare.domain.service;

import com.example.solicare.domain.dto.FcmSendResult;
import com.example.solicare.domain.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FcmPushService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public FcmSendResult sendMessageToMember(String memberUuid, String title, String body) {
        return memberRepository.findMemberByUuid(memberUuid)
                .map(member -> Optional.ofNullable(member.getFcmDevice())
                        .map(fcmDevice -> sendMessageTo(fcmDevice.getToken(), title, body))
                        .orElse(FcmSendResult.of(FcmSendResult.Status.NOT_REGISTER, null)))
                .orElseGet(() -> FcmSendResult.of(FcmSendResult.Status.MEMBER_NOT_FOUND, null));
    }

    public FcmSendResult sendMessageTo(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            firebaseMessaging.send(message);
            return FcmSendResult.of(FcmSendResult.Status.SENT, null);
        } catch (Exception e) {
            return FcmSendResult.of(FcmSendResult.Status.ERROR, e);
        }
    }
}
