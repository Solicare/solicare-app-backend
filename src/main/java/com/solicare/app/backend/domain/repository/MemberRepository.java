package com.solicare.app.backend.domain.repository;

import com.solicare.app.backend.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
  Optional<Member> findByUuid(String uuid);

  Optional<Member> findByEmail(String email);

  boolean existsByUuid(String uuid);

  boolean existsByEmail(String email);

  Optional<Member> findByPushDevices_Uuid(String pushDeviceUuid);
}
