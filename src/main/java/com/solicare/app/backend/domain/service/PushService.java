package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.domain.dto.FcmSendResult;
import com.solicare.app.backend.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushService {
  private final PushDeviceService pushDeviceService;

  public FcmSendResult sendFcmMessageToMemberByUuid(String memberUuid, String title, String body) {
    return null; // Placeholder
  }

  public FcmSendResult sendFcmMessageToMemberByEmail(String email, String title, String body) {
    return null; // Placeholder
  }

  public FcmSendResult sendFcmMessageToMemberByPhoneNumber(
      String phoneNumber, String title, String body) {
    return null; // Placeholder
  }

  private FcmSendResult sendFcmMessageToMember(Member member, String title, String body) {
    return null; // Placeholder
  }

  // TODO: Implement above methods
}
