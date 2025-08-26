package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.domain.dto.output.member.MemberJoinOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberLoginOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberProfileOutput;
import com.solicare.app.backend.domain.service.MemberService;
import com.solicare.app.backend.global.apiPayload.ApiResponse;
import com.solicare.app.backend.global.apiPayload.response.status.ErrorStatus;
import com.solicare.app.backend.global.apiPayload.response.status.SuccessStatus;

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
        // TODO: check result of creation and respond accordingly via ApiResponse<T>
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result));
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
        // TODO: check result of login and respond accordingly via ApiResponse<T>
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result));
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
            // TODO: respond via ApiResponse<T> by global-access-exception-handler
            //  with details(Reason, roles required, roles current have, self-allowed, etc)
            return ResponseEntity.status(403)
                    .body(
                            ApiResponse.onFailure(
                                    ErrorStatus._FORBIDDEN.getCode(),
                                    ErrorStatus._FORBIDDEN.getMessage(),
                                    null));
        }
        MemberProfileOutput result = memberService.getProfile(uuid);
        // TODO: check result of login and respond accordingly via ApiResponse<T>
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result));
    }
}
