package com.solicare.app.backend.domain.repository;

import com.solicare.app.backend.domain.entity.PushDevice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushDeviceRepository extends JpaRepository<PushDevice, String> {
  Optional<PushDevice> findByUuid(String uuid);

  Optional<PushDevice> findByToken(String token);

  void deleteByToken(String token);
}
