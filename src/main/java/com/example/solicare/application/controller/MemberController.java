package com.example.solicare.application.controller;

import com.example.solicare.application.dto.member.MemberJoinRequestDTO;
import com.example.solicare.application.dto.member.MemberLoginRequestDTO;
import com.example.solicare.application.dto.member.MemberLoginResponseDTO;
import com.example.solicare.domain.service.MemberService;
import com.example.solicare.global.apiPayload.ApiResponse;
import com.example.solicare.global.apiPayload.response.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복 회원")
    })
    @PostMapping("/join")
    public ResponseEntity<?> memberCreate(@RequestBody @Valid MemberJoinRequestDTO memberJoinRequestDTO) {
        MemberLoginResponseDTO resp = memberService.create(memberJoinRequestDTO);
        // TODO: check result of creation and respond accordingly
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, resp));
    }

    @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "자격 증명 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberLoginResponseDTO resp = memberService.loginAndIssueToken(memberLoginRequestDTO);
        // TODO: check result of creation and respond accordingly
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, resp));
    }

    @GetMapping("profile?uuid={uuid}")
    public ResponseEntity<?> getProfile(@NonNull Authentication authentication, @PathVariable("uuid") String uuid) {
        // TODO: check if the authenticated user matches the requested uuid
        // TODO: fetch and return the profile information
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }
}
