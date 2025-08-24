package com.solicare.app.backend.domain.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.solicare.app.backend.domain.dto.FcmSendResult;
import com.solicare.app.backend.domain.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {
  private final FirebaseMessaging firebaseMessaging;
  private final MemberRepository memberRepository;

  public FcmSendResult sendMessageToMember(String memberUuid, String title, String body) {
    return memberRepository
        .findByUuid(memberUuid)
        .map(
            member ->
                Optional.ofNullable(member.getPushDevice())
                    .map(fcmDevice -> sendMessageTo(fcmDevice.getToken(), title, body))
                    .orElse(FcmSendResult.of(FcmSendResult.Status.NOT_REGISTER, null)))
        .orElseGet(() -> FcmSendResult.of(FcmSendResult.Status.MEMBER_NOT_FOUND, null));
  }

  public FcmSendResult sendMessageTo(String token, String title, String body) {
    Message message =
        Message.builder()
            .setToken(token)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .build();

    try {
      firebaseMessaging.send(message);
      return FcmSendResult.of(FcmSendResult.Status.SENT, null);
    } catch (Exception e) {
      return FcmSendResult.of(FcmSendResult.Status.ERROR, e);
    }
  }
}
