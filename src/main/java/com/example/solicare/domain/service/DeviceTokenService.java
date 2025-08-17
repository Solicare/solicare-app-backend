package com.example.solicare.domain.service;

import com.example.solicare.domain.entity.DeviceToken;
import com.example.solicare.domain.repository.DeviceTokenRepository;
import com.example.solicare.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class DeviceTokenService {
    private final MemberRepository memberRepository;
    private final DeviceTokenRepository repository;
    private final FcmService fcmService;

    public void register(String phoneNumber, String token) {
        Optional<DeviceToken> queriedToken = repository.findByPhoneNumber(phoneNumber);
        if (queriedToken.isEmpty()) {
            DeviceToken newDeviceToken = DeviceToken.builder()
                    .memberId(null)
                    .phoneNumber(phoneNumber)
                    .token(token)
                    .build();
            repository.save(newDeviceToken);
            return;
        }

        DeviceToken deviceToken = queriedToken.get();
        if (!deviceToken.getToken().equals(token)) {
            deviceToken.update(token);
            repository.save(deviceToken);
        }
    }

    public void unregisterByToken(String token) {
        repository.deleteDeviceTokenByToken(token);
    }
}
