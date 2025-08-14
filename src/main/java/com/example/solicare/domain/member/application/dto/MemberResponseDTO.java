package com.example.solicare.domain.member.application.dto;

import com.example.solicare.domain.member.domain.Gender;
import com.example.solicare.domain.member.domain.Member;
import com.example.solicare.domain.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDTO {
    private Long id;
    private String name;
    private String elderlyName;
    private String phoneNumber;
    private Role role;
    private Gender gender;
    private String address;
    private Integer age;
    private String specialNote;

    public static MemberResponseDTO from(Member m) {
        return MemberResponseDTO.builder()
                .id(m.getId())
                .name(m.getName())
                .elderlyName(m.getElderlyName())
                .phoneNumber(m.getPhoneNumber())
                .role(m.getRole())
                .gender(m.getGender())
                .address(m.getAddress())
                .age(m.getAge())
                .specialNote(m.getSpecialNote())
                .build();
    }
}
