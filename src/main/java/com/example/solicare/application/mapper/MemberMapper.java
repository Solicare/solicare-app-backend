package com.example.solicare.application.mapper;

import com.example.solicare.application.dto.member.MemberJoinRequestDTO;
import com.example.solicare.application.dto.member.MemberProfileResponseDTO;
import com.example.solicare.domain.entity.Member;
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
