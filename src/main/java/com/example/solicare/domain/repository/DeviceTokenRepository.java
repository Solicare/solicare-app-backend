package com.example.solicare.domain.repository;

import com.example.solicare.domain.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByPhoneNumber(String phoneNumber);

    Optional<DeviceToken> findByMemberId(Long memberId);

    void deleteDeviceTokenByToken(String token);
}
