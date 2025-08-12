package com.example.solicare.domain.member.presentation;

import com.example.solicare.domain.member.application.dto.MemberLoginRequestDTO;
import com.example.solicare.domain.member.application.dto.MemberResponseDTO;
import com.example.solicare.domain.member.application.dto.MemberSaveRequestDTO;
import com.example.solicare.domain.member.domain.Member;
import com.example.solicare.domain.member.domain.MemberService;
import com.example.solicare.global.apiPayload.ApiResponse;
import com.example.solicare.global.apiPayload.code.status.SuccessStatus;
import com.example.solicare.global.auth.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<?> memberCreate(@RequestBody @Valid MemberSaveRequestDTO dto){
        Member member = memberService.create(dto);
        return ResponseEntity.ok(ApiResponse.onSuccess(
                SuccessStatus._OK,
                MemberResponseDTO.from(member) // ← 엔티티 대신 응답 DTO
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        Member member=memberService.login(memberLoginRequestDTO);
        String jwtToken=jwtTokenProvider.createToken(member.getPhoneNumber(),member.getRole().toString());

        Map<String,Object> loginInfo=new HashMap<>();
        loginInfo.put("id",member.getId());
        loginInfo.put("token",jwtToken);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,loginInfo));
    }
}
