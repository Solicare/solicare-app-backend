package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.domain.dto.output.member.MemberJoinOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberLoginOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberProfileOutput;
import com.solicare.app.backend.domain.service.MemberService;
import com.solicare.app.backend.global.apiPayload.ApiResponse;

import com.solicare.app.backend.global.apiPayload.exception.CustomException;
import com.solicare.app.backend.global.apiPayload.response.status.GlobalStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "회원가입 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "409",
                description = "중복 회원")
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<MemberJoinOutput>> join(
            @Schema(name = "MemberRequestJoin", description = "회원가입 요청 DTO") @RequestBody @Valid
                    MemberRequestDTO.Join memberJoinRequestDTO) {
        MemberJoinOutput result = memberService.createAndIssueToken(memberJoinRequestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberLoginOutput>> login(
            @RequestBody @Valid MemberRequestDTO.Login memberLoginRequestDTO) {
        MemberLoginOutput result = memberService.loginAndIssueToken(memberLoginRequestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    @PreAuthorize("hasAuthority('ROLE_MEMBER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MemberProfileOutput>> getProfile(
            @NonNull Authentication authentication, @RequestParam("uuid") String uuid) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        System.out.print(authentication.getName());
        if (!isAdmin && !authentication.getName().equals(uuid)) {
            throw new CustomException(GlobalStatus._FORBIDDEN);
        }
        MemberProfileOutput result = memberService.getProfile(uuid);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }
}
