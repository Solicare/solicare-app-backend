package com.example.solicare.domain.repository;

import com.example.solicare.domain.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeniorRepository extends JpaRepository<Senior, String> {
    Optional<Senior> findSeniorByUserId(String userid);

    Optional<Senior> findSeniorByPhoneNumber(String phoneNumber);
}
