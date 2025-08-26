package com.solicare.app.backend.domain.repository;

import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.entity.PushDevice;
import com.solicare.app.backend.domain.enums.PushDeviceType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushDeviceRepository extends JpaRepository<PushDevice, String> {
    List<PushDevice> findByMember(Member member);

    List<PushDevice> findByMember_Uuid(String memberUuid);

    void deleteByTypeAndToken(PushDeviceType type, String token);

    boolean existsByTypeAndToken(PushDeviceType type, String token);
}
