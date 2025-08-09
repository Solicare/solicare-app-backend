package com.example.solicare.domain.member.domain;

import com.example.solicare.domain.member.application.dto.MemberLoginRequestDTO;
import com.example.solicare.domain.member.application.dto.MemberSaveRequestDTO;
import com.example.solicare.domain.member.infrastructure.MemberRepository;
import com.example.solicare.global.apiPayload.exception.custom.DuplicateMemberException;
import com.example.solicare.global.apiPayload.exception.custom.InvalidCredentialsException;
import com.example.solicare.global.apiPayload.exception.custom.MemberNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(MemberSaveRequestDTO memberSaveRequestDTO){
        if(memberRepository.findByPhoneNumber(memberSaveRequestDTO.getPhoneNumber()).isPresent()){
            throw new DuplicateMemberException();
        }

        Member newMember = Member.builder()
                .name(memberSaveRequestDTO.getName())
                .elderlyName(memberSaveRequestDTO.getElderlyName())
                .phoneNumber(memberSaveRequestDTO.getPhoneNumber())
                .password(passwordEncoder.encode(memberSaveRequestDTO.getPassword())) // ★ 인코딩 추가
                .build();

        return memberRepository.save(newMember);
    }


    public Member login(MemberLoginRequestDTO memberLoginRequestDTO){
        // 회원 존재 여부 확인
        Member member=memberRepository.findByPhoneNumber(memberLoginRequestDTO.getPhoneNumber())
                .orElseThrow(MemberNotFoundException::new);

        if(!passwordEncoder.matches(memberLoginRequestDTO.getPassword(), member.getPassword())){
            throw new InvalidCredentialsException();
        }
        return member;
    }

}
