package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.request.SeniorRequestDTO;
import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.application.mapper.SeniorMapper;
import com.solicare.app.backend.domain.dto.output.senior.SeniorCreateOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorLoginOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorProfileOutput;
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
    private final PasswordEncoder passwordEncoder;
    private final SeniorRepository seniorRepository;
    private final SeniorMapper seniorMapper;

    /** Senior 회원 가입 및 JWT 토큰 발급 */
    public SeniorCreateOutput createAndIssueToken(SeniorRequestDTO.Create dto) {
        if (seniorRepository.existsByUserId(dto.userId())) {
            return SeniorCreateOutput.of(
                    SeniorCreateOutput.Status.SENIOR_ALREADY_EXISTS, null, null);
        }
        Senior newSenior = seniorMapper.toEntity(dto);
        seniorRepository.save(newSenior);

        String jwtToken = jwtTokenProvider.createToken(List.of(Role.SENIOR), newSenior.getUuid());
        return SeniorCreateOutput.of(
                SeniorCreateOutput.Status.SUCCESS, new SeniorResponseDTO.Create(jwtToken), null);
    }

    /** Senior 로그인 및 JWT 토큰 발급 */
    public SeniorLoginOutput loginAndIssueToken(SeniorRequestDTO.Login dto) {
        Senior senior = seniorRepository.findByUserId(dto.userId()).orElse(null);
        if (senior == null) {
            return SeniorLoginOutput.of(SeniorLoginOutput.Status.SENIOR_NOT_FOUND, null, null);
        }

        if (!passwordEncoder.matches(dto.password(), senior.getPassword())) {
            return SeniorLoginOutput.of(SeniorLoginOutput.Status.INVALID_PASSWORD, null, null);
        }

        String jwtToken = jwtTokenProvider.createToken(List.of(Role.SENIOR), senior.getUuid());
        return SeniorLoginOutput.of(
                SeniorLoginOutput.Status.SUCCESS,
                new SeniorResponseDTO.Login(senior.getName(), jwtToken),
                null);
    }

    public SeniorProfileOutput getProfile(String uuid) {
        Senior senior = seniorRepository.findByUuid(uuid).orElse(null);
        if (senior == null) {
            return SeniorProfileOutput.of(SeniorProfileOutput.Status.NOT_FOUND, null, null);
        }
        SeniorResponseDTO.Profile profile = seniorMapper.toProfileDTO(senior);
        return SeniorProfileOutput.of(SeniorProfileOutput.Status.SUCCESS, profile, null);
    }
}
