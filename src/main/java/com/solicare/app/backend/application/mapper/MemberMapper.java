package com.solicare.app.backend.application.mapper;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.entity.Member;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {
    private final PasswordEncoder passwordEncoder;

    public Member toEntity(MemberRequestDTO.Join dto) {
        return Member.builder()
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();
    }

    public MemberResponseDTO.Profile toProfileDTO(Member member) {
        return new MemberResponseDTO.Profile(
                member.getName(), member.getEmail(), member.getPhoneNumber());
    }
}
