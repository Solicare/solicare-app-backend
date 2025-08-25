package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.application.mapper.MemberMapper;
import com.solicare.app.backend.domain.dto.output.member.MemberJoinOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberLoginOutput;
import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.global.auth.JwtTokenProvider;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    // ================== 회원 가입 ==================
    /**
     * 회원 가입 및 JWT 토큰 발급
     *
     * @param dto 회원 가입 요청 DTO
     * @return 가입 결과 및 토큰 정보
     */
    public MemberJoinOutput createAndIssueToken(MemberRequestDTO.Join dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            return MemberJoinOutput.of(MemberJoinOutput.Status.USER_ALREADY_EXISTS, null, null);
        }
        Member newMember = memberMapper.toEntity(dto);
        memberRepository.save(newMember);
        String jwtToken = jwtTokenProvider.createToken(newMember.getEmail(), newMember.getUuid());
        return MemberJoinOutput.of(
                MemberJoinOutput.Status.SUCCESS, new MemberResponseDTO.Join(jwtToken), null);
    }

    // ================== 로그인 (토큰 발급) ==================
    /**
     * 로그인 및 JWT 토큰 발급
     *
     * @param dto 로그인 요청 DTO
     * @return 로그인 결과 및 토큰 정보
     */
    public MemberLoginOutput loginAndIssueToken(MemberRequestDTO.Login dto) {
        Member member = memberRepository.findByEmail(dto.email()).orElse(null);
        if (member == null) {
            return MemberLoginOutput.of(MemberLoginOutput.Status.USER_NOT_FOUND, null, null);
        }
        if (!passwordEncoder.matches(dto.password(), member.getPassword())) {
            return MemberLoginOutput.of(MemberLoginOutput.Status.INVALID_PASSWORD, null, null);
        }
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getUuid());
        return MemberLoginOutput.of(
                MemberLoginOutput.Status.SUCCESS,
                new MemberResponseDTO.Login(member.getName(), jwtToken),
                null);
    }
}
