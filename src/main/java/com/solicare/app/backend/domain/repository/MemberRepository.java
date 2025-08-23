package com.solicare.app.backend.domain.repository;

import com.solicare.app.backend.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findMemberByUuid(String uuid);

    Optional<Member> findMemberByEmail(String email);

    boolean existsByEmail(String email);
}
