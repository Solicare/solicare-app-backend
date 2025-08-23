package com.example.solicare.domain.service;

import com.example.solicare.domain.entity.FcmDevice;
import com.example.solicare.domain.repository.FcmDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FcmDeviceService {
    private final FcmDeviceRepository repository;

    public FcmDevice register(String token, String localNumber) {
        Optional<FcmDevice> queried = repository.findByLocalNumber(localNumber);
        return queried.map(
                        fcmDevice -> repository.save(
                                fcmDevice.update(token, localNumber)
                        ))
                .orElseGet(() -> repository.save(FcmDevice.builder()
                        .localNumber(localNumber)
                        .token(token)
                        .build()));
    }

    public void unregister(String token) {
        // TODO: implement this method
    }

    public void unregisterByLocalNumber(String localNumber) {
        repository.findByLocalNumber(localNumber)
                .ifPresent(fcmDevice -> {
                    fcmDevice.unregister();
                    repository.save(fcmDevice);
                });
    }

    public void unregisterByToken(String token) {
        repository.deleteFcmDeviceByToken(token);
    }
}
