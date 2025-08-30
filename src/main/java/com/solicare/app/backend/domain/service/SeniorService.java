package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.request.SeniorAuthRequestDTO;
import com.solicare.app.backend.application.dto.res.SeniorAuthResponseDTO;
import com.solicare.app.backend.application.mapper.SeniorMapper;
import com.solicare.app.backend.domain.dto.output.senior.SeniorJoinOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorLoginOutput;
import com.solicare.app.backend.domain.entity.Senior;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.repository.SeniorRepository;
import com.solicare.app.backend.global.auth.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SeniorService {

    private final JwtTokenProvider jwtTokenProvider;
    private final SeniorRepository seniorRepository;
    private final SeniorMapper seniorMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Senior 회원 가입 및 JWT 토큰 발급
     */
    public SeniorJoinOutput createAndIssueToken(SeniorAuthRequestDTO.Join dto) {
        if (seniorRepository.existsByUserId(dto.userId())) {
            return SeniorJoinOutput.of(SeniorJoinOutput.Status.USER_ALREADY_EXISTS, null, null);
        }
        Senior newSenior = seniorMapper.toEntity(dto);
        seniorRepository.save(newSenior);

        // JWT 토큰 생성 (Role: SENIOR, Subject: userId)
        String jwtToken = jwtTokenProvider.createToken(List.of(Role.SENIOR), newSenior.getUserId());

        return SeniorJoinOutput.of(
                SeniorJoinOutput.Status.SUCCESS, new SeniorAuthResponseDTO.Join(jwtToken), null);
    }

    /**
     * Senior 로그인 및 JWT 토큰 발급
     */
    public SeniorLoginOutput loginAndIssueToken(SeniorAuthRequestDTO.Login dto) {
        Senior senior = seniorRepository.findByUserId(dto.userId()).orElse(null);
        if (senior == null) {
            return SeniorLoginOutput.of(SeniorLoginOutput.Status.USER_NOT_FOUND, null, null);
        }

        if (!passwordEncoder.matches(dto.password(), senior.getPassword())) {
            return SeniorLoginOutput.of(SeniorLoginOutput.Status.INVALID_PASSWORD, null, null);
        }

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(List.of(Role.SENIOR), senior.getUserId());

        return SeniorLoginOutput.of(
                SeniorLoginOutput.Status.SUCCESS,
                new SeniorAuthResponseDTO.Login(senior.getName(), jwtToken),
                null);
    }
}