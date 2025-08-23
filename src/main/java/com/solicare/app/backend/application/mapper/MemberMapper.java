package com.solicare.app.backend.application.mapper;

import com.solicare.app.backend.application.dto.member.MemberJoinRequestDTO;
import com.solicare.app.backend.application.dto.member.MemberProfileResponseDTO;
import com.solicare.app.backend.domain.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberMapper {
    private final PasswordEncoder passwordEncoder;

    public Member toEntity(MemberJoinRequestDTO dto) {
        return Member.of(
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                passwordEncoder.encode(dto.getPassword())
        );
    }

    public MemberProfileResponseDTO toProfileDTO(Member member) {
        return new MemberProfileResponseDTO(
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber()
        );
    }
}
