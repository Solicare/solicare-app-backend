package com.example.solicare.application.controller;

import com.example.solicare.application.dto.member.MemberLoginRequestDTO;
import com.example.solicare.application.dto.member.MemberSaveRequestDTO;
import com.example.solicare.domain.entity.Member;
import com.example.solicare.domain.service.MemberService;
import com.example.solicare.global.apiPayload.ApiResponse;
import com.example.solicare.global.apiPayload.response.status.SuccessStatus;
import com.example.solicare.global.auth.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복 회원")
    })
    @PostMapping("/join")
    public ResponseEntity<?> memberCreate(
            @RequestBody @Valid MemberSaveRequestDTO memberSaveRequestDTO) {

        Member member = memberService.create(memberSaveRequestDTO);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, member));
    }

    @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "자격 증명 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid MemberLoginRequestDTO memberLoginRequestDTO) {

        Member member = memberService.login(memberLoginRequestDTO);
        String jwtToken = jwtTokenProvider.createToken(
                member.getPhoneNumber(), member.getRole().toString()
        );

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, loginInfo));
    }
}
