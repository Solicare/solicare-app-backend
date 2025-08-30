package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.application.mapper.MemberMapper;
import com.solicare.app.backend.domain.dto.output.member.MemberJoinOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberLoginOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberProfileOutput;
import com.solicare.app.backend.domain.service.MemberService;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiResponseFactory;
import com.solicare.app.backend.global.res.ApiStatus;

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

import java.util.Optional;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberController {
    private final MemberService memberService;
    private final ApiResponseFactory apiResponseFactory;
    private final MemberMapper memberMapper;

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
        return apiResponseFactory.onSuccess(result);
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
        return apiResponseFactory.onSuccess(result);
    }

    @PreAuthorize("hasAuthority('ROLE_MEMBER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getProfile(
            @NonNull Authentication authentication, @RequestParam("uuid") String uuid) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        System.out.print(authentication.getName());
        if (!isAdmin && !authentication.getName().equals(uuid)) {
            return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "본인의 정보만 조회 가능합니다.");
        }
        MemberProfileOutput result = memberService.getProfile(uuid);
        if (!result.isSuccess()) {
            // 에러 메시지 출력하기
        }
        return apiResponseFactory.onSuccess(result.getResponse());
    }

    @Operation(summary = "돌봄 시니어 목록 조회", description = "현재 로그인된 회원이 돌보고 있는 시니어 목록을 조회합니다.")
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    @GetMapping("/seniors/caring")
    public ResponseEntity<ApiResponse<Object>> getSeniorsCaredByMember(@NonNull Authentication authentication) {
        String memberUuid = authentication.getName();
        Optional<MemberResponseDTO.Seniors> result = memberService.getSeniorsUnderCare(memberUuid);

        return result
                .<ResponseEntity<ApiResponse<Object>>>map(apiResponseFactory::onSuccess)
                .orElseGet(() -> apiResponseFactory.onFailure(ApiStatus._NOT_FOUND, "해당 회원을 찾을 수 없습니다."));
    }
}
