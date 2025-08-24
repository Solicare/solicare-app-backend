package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.domain.repository.PushDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PushDeviceService {
  private final PushDeviceRepository repository;

  //  public PushDevice register(String token) {
  //    Optional<PushDevice> queried = repository.findByToken(token);
  //    return queried
  //        .map(pushDevice -> repository.save(pushDevice.update(token)))
  //        .orElseGet(
  //            () ->
  //                repository.save(
  //                    PushDevice.builder().localNumber(localNumber).token(token).build()));
  //  }
  //
  //  public void unregisterByLocalNumber(String localNumber) {
  //    repository
  //        .findByLocalNumber(localNumber)
  //        .ifPresent(
  //            pushDevice -> {
  //              repository.save(pushDevice.setEnabled(false));
  //            });
  //  }
  //
  //  public void unregisterByToken(String token) {
  //    repository.deleteFcmDeviceByToken(token);
  //  }

  // TODO: Implement commented logic
}
