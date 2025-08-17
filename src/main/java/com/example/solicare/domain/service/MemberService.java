package com.example.solicare.domain.service;

import com.example.solicare.application.dto.member.MemberLoginRequestDTO;
import com.example.solicare.application.dto.member.MemberSaveRequestDTO;
import com.example.solicare.domain.entity.Member;
import com.example.solicare.domain.repository.MemberRepository;
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

    public Member create(MemberSaveRequestDTO memberSaveRequestDTO) {
        if (memberRepository.findByPhoneNumber(memberSaveRequestDTO.getPhoneNumber()).isPresent()) {
            throw new DuplicateMemberException();
        }

        Member newMember = Member.builder()
                .name(memberSaveRequestDTO.getName())
                .elderlyName(memberSaveRequestDTO.getElderlyName())
                .phoneNumber(memberSaveRequestDTO.getPhoneNumber())
                .password(passwordEncoder.encode(memberSaveRequestDTO.getPassword())) // ★ 인코딩 추가
                .gender(memberSaveRequestDTO.getGender()) // ★ 성별 추가
                .address(memberSaveRequestDTO.getAddress()) // ★ 주소 추가
                .age(memberSaveRequestDTO.getAge()) // ★ 나이 추가
                .specialNote(memberSaveRequestDTO.getSpecialNote()) // ★ 특이사항 추가
                .build();

        return memberRepository.save(newMember);
    }

    public Member login(MemberLoginRequestDTO memberLoginRequestDTO) {
        // 회원 존재 여부 확인
        Member member = memberRepository.findByPhoneNumber(memberLoginRequestDTO.getPhoneNumber())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(memberLoginRequestDTO.getPassword(), member.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return member;
    }

}
