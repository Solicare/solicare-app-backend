package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.output.care.CareLinkOutput;
import com.solicare.app.backend.domain.dto.output.care.CareQueryOutput;
import com.solicare.app.backend.domain.dto.output.member.*;
import com.solicare.app.backend.domain.service.CareService;
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

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberController {
    private final MemberService memberService;
    private final CareService careService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "멤버 회원가입", description = "새로운 사용자를 등록합니다.")
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
        // TODO: respond with appropriate status based on result not MemberJoinOutput
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(summary = "멤버 로그인", description = "전화번호와 비밀번호로 로그인합니다.")
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
        // TODO: respond with appropriate status based on result not MemberLoginOutput
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(summary = "멤버 정보조회", description = "멤버 UUID로 회원정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    @GetMapping("/profile")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
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
        // TODO: respond with appropriate status based on result not MemberProfileOutput
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(
            summary = "모니터링 대상 목록 조회",
            description = "특정 회원의 UUID로, 해당 회원이 모니터링하는 시니어(모니터링 대상) 목록을 조회합니다.")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/{memberUuid}/seniors")
    public ResponseEntity<ApiResponse<Object>> getCareSeniors(
            @PathVariable String memberUuid, @NonNull Authentication authentication) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !authentication.getName().equals(memberUuid)) {
            return apiResponseFactory.onFailure(
                    ApiStatus._FORBIDDEN, "본인만 자신의 모니터링 대상 목록을 조회할 수 있습니다.");
        }
        CareQueryOutput<SeniorResponseDTO.Profile> result =
                careService.querySeniorByMember(memberUuid);
        // TODO: respond with appropriate status based on result not
        //  CareQueryOutput<SeniorResponseDTO.Profile>
        return apiResponseFactory.onSuccess(result.getResponse());
    }

    @Operation(summary = "모니터링 대상 추가", description = "특정 회원의 UUID로, 해당 회원의 모니터링 대상(시니어)을 추가합니다.")
    @PostMapping("/{memberUuid}/seniors")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> addCareSenior(
            @PathVariable String memberUuid,
            @NonNull Authentication authentication,
            @RequestBody @Valid MemberRequestDTO.LinkSenior requestDto) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !authentication.getName().equals(memberUuid)) {
            return apiResponseFactory.onFailure(
                    ApiStatus._FORBIDDEN, "본인만 자신의 모니터링 대상을 추가할 수 있습니다");
        }
        CareLinkOutput<MemberResponseDTO.Profile> result =
                careService.linkSeniorToMember(memberUuid, requestDto);
        if (!result.isSuccess()) {
            ApiStatus status =
                    switch (result.getStatus()) {
                        case MEMBER_NOT_FOUND, SENIOR_NOT_FOUND -> ApiStatus._NOT_FOUND;
                        case INVALID_SENIOR_PASSWORD -> ApiStatus._UNAUTHORIZED;
                        case ALREADY_LINKED -> ApiStatus._BAD_REQUEST;
                        default -> ApiStatus._INTERNAL_SERVER_ERROR;
                    };
            return apiResponseFactory.onFailure(status, result.getStatus().name());
            // TODO: improve body content not just status name, maybe with message field on enum
        }
        return apiResponseFactory.onSuccess(result.getResponse());
    }
}
