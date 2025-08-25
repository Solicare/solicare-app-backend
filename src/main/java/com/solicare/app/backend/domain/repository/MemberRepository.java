package com.solicare.app.backend.domain.repository;

import com.solicare.app.backend.domain.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByEmail(String email);

    boolean existsByUuid(String uuid);

    boolean existsByEmail(String email);

    Optional<Member> findByPushDevices_Uuid(String pushDeviceUuid);

    Optional<Member> findByPhoneNumber(String phoneNumber);
}
