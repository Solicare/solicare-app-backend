package com.example.solicare.domain.repository;

import com.example.solicare.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPhoneNumber(String phoneNumber);

    Optional<Member> findMemberIdByPhoneNumber(String phoneNumber);
}
