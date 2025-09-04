package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.domain.dto.push.PushBatchProcessResult;
import com.solicare.app.backend.domain.dto.push.PushDeliveryResult;
import com.solicare.app.backend.domain.enums.Push;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.domain.repository.SeniorRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PushService {
    private final DeviceService deviceService;
    private final FirebaseService firebaseService;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;

    public PushBatchProcessResult push(Role role, String uuid, String title, String body) {
        if (!existsByRoleAndUuid(role, uuid)) {
            return PushBatchProcessResult.of(null, PushBatchProcessResult.Status.NOT_FOUND);
        }
        List<PushDeliveryResult> PushDeliveryResultList =
                deviceService.getDevices(role, uuid).getResponse().stream()
                        .filter(DeviceResponseDTO.Info::enabled)
                        .map(
                                (device) -> {
                                    if (Objects.requireNonNull(device.type()) == Push.FCM) {
                                        return firebaseService.sendMessageTo(
                                                device.token(), title, body);
                                    }
                                    return PushDeliveryResult.of(
                                            PushDeliveryResult.Status.ERROR,
                                            new IllegalArgumentException(
                                                    "Unsupported push type: " + device.type()));
                                })
                        .toList();
        return PushBatchProcessResult.of(PushDeliveryResultList).setStatusByDetails();
    }

    // TODO: extract this method and remove duplicated code in Service classes
    private boolean existsByRoleAndUuid(Role role, String uuid) {
        return switch (role) {
            case MEMBER -> memberRepository.existsByUuid(uuid);
            case SENIOR -> seniorRepository.existsByUuid(uuid);
            default -> false;
        };
    }
}
