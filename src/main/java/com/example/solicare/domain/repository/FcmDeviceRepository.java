package com.example.solicare.domain.repository;

import com.example.solicare.domain.entity.FcmDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmDeviceRepository extends JpaRepository<FcmDevice, String> {
    Optional<FcmDevice> findByLocalNumber(String localNumber);

    void deleteFcmDeviceByToken(String token);
}
