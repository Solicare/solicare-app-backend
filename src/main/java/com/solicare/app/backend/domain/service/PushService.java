package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.domain.dto.output.push.FcmSendOutputDetail;
import com.solicare.app.backend.domain.dto.output.push.PushSendOutput;
import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.enums.PushDeviceType;
import com.solicare.app.backend.domain.repository.MemberRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PushService {
    private final FcmService fcmService;
    private final MemberRepository memberRepository;
    private final PushDeviceService pushDeviceService;

    public PushSendOutput<FcmSendOutputDetail> sendFcmMessageToMemberByUuid(
            String memberUuid, String title, String body) {
        Optional<Member> member = memberRepository.findByUuid(memberUuid);
        if (member.isPresent()) {
            return sendFcmMessageToMember(member.get(), title, body);
        }
        return PushSendOutput.of(null, PushSendOutput.Status.MEMBER_NOT_FOUND);
    }

    private PushSendOutput<FcmSendOutputDetail> sendFcmMessageToMember(
            Member member, String title, String body) {
        List<FcmSendOutputDetail> sendOutputDetailList =
                pushDeviceService.getPushDevicesByMember(member).stream()
                        .filter(device -> device.type() == PushDeviceType.FCM && device.enabled())
                        .map(device -> fcmService.sendMessageTo(device.token(), title, body))
                        .toList();
        return PushSendOutput.of(sendOutputDetailList).setStatusByDetails();
    }

    public PushSendOutput<FcmSendOutputDetail> sendFcmMessageToMemberByEmail(
            String email, String title, String body) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return sendFcmMessageToMember(member.get(), title, body);
        }
        return PushSendOutput.of(null, PushSendOutput.Status.MEMBER_NOT_FOUND);
    }

    public PushSendOutput<FcmSendOutputDetail> sendFcmMessageToMemberByPhoneNumber(
            String phoneNumber, String title, String body) {
        Optional<Member> member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member.isPresent()) {
            return sendFcmMessageToMember(member.get(), title, body);
        }
        return PushSendOutput.of(null, PushSendOutput.Status.MEMBER_NOT_FOUND);
    }
}
