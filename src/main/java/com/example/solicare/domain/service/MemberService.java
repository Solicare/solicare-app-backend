package com.example.solicare.domain.service;

import com.example.solicare.application.dto.member.MemberJoinRequestDTO;
import com.example.solicare.application.dto.member.MemberLoginRequestDTO;
import com.example.solicare.application.dto.member.MemberLoginResponseDTO;
import com.example.solicare.application.mapper.MemberMapper;
import com.example.solicare.domain.entity.Member;
import com.example.solicare.domain.repository.MemberRepository;
import com.example.solicare.global.auth.JwtTokenProvider;
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

    /* ========== 가입 ========== */
    public MemberLoginResponseDTO create(MemberJoinRequestDTO dto) {
        if (memberRepository.existsByEmail(dto.getPhoneNumber())) {
            // TODO: return duplicate email result
            return null; // TODO:REMOVE: Placeholder return statement
        }

        Member newMember = memberMapper.toEntity(dto);
        memberRepository.save(newMember);

        // TODO: implement token issuance after registration
        return null; // TODO:REMOVE: Placeholder return statement
    }

    /* ========== 로그인 (토큰 발급) ========== */
    public MemberLoginResponseDTO loginAndIssueToken(MemberLoginRequestDTO dto) {
        Member member = memberRepository.findMemberByEmail(dto.getEmail()).orElse(null);
        if (member == null) {
            // TODO: return member not found result
            return null; // TODO:REMOVE: Placeholder return statement
        }

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            // TODO: return password mismatch result
            return null; // TODO:REMOVE: Placeholder return statement
        }
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getUuid());
        return new MemberLoginResponseDTO(member.getName(), jwtToken);
    }
}
