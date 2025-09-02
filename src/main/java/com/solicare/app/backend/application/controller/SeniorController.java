package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.CareRequestDTO;
import com.solicare.app.backend.application.dto.request.SeniorRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.output.care.CareLinkOutput;
import com.solicare.app.backend.domain.dto.output.care.CareQueryOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorCreateOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorLoginOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorProfileOutput;
import com.solicare.app.backend.domain.service.CareService;
import com.solicare.app.backend.domain.service.SeniorService;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiResponseFactory;
import com.solicare.app.backend.global.res.ApiStatus;

import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Senior", description = "모니터링 대상 관련 API")
@RestController
@RequestMapping("/api/senior")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SeniorController {
    private final SeniorService seniorService;
    private final CareService careService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "시니어 회원가입", description = "새로운 모니터링 대상(시니어)을 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "회원가입 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 모니터링 대상 사용자 ID")
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<SeniorCreateOutput>> join(
            @RequestBody @Valid SeniorRequestDTO.Create createRequestDTO) {
        SeniorCreateOutput result = seniorService.createAndIssueToken(createRequestDTO);
        // TODO: respond with appropriate status based on result not SeniorCreateOutput
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(summary = "시니어 로그인", description = "모니터링 대상(시니어)의 사용자 ID와 비밀번호로 로그인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SeniorLoginOutput>> login(
            @RequestBody @Valid SeniorRequestDTO.Login loginRequestDTO) {
        SeniorLoginOutput result = seniorService.loginAndIssueToken(loginRequestDTO);
        // TODO: respond with appropriate status based on result not SeniorLoginOutput
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(summary = "시니어 정보조회", description = "시니어의 UUID를 이용해 해당 시니어의 상세 프로필 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_SENIOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> getProfile(
            @NonNull Authentication authentication, @RequestParam("uuid") String uuid) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !authentication.getName().equals(uuid)) {
            return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "본인의 정보만 조회 가능합니다.");
        }
        SeniorProfileOutput result = seniorService.getProfile(uuid);
        // TODO: respond with appropriate status based on result not SeniorProfileOutput
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(
            summary = "모니터링 보호자 목록 조회",
            description = "특정 시니어의 UUID로, 해당 시니어를 모니터링하는 보호자(멤버) 목록을 조회합니다.")
    @PreAuthorize("hasAuthority('ROLE_SENIOR')")
    @GetMapping("/{seniorUuid}/care/members")
    public ResponseEntity<ApiResponse<Object>> getCareMembers(
            @PathVariable String seniorUuid, @NonNull Authentication authentication) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !authentication.getName().equals(seniorUuid)) {
            return apiResponseFactory.onFailure(
                    ApiStatus._FORBIDDEN, "본인만 자신의 보호자 목록을 조회할 수 있습니다.");
        }
        CareQueryOutput<MemberResponseDTO.Profile> result =
                careService.queryMemberBySenior(seniorUuid);
        // TODO: respond with appropriate status based on result not
        //  CareQueryOutput<MemberResponseDTO.Profile>
        return apiResponseFactory.onSuccess(result.getResponse());
    }

    @Operation(
            summary = "모니터링 보호자 추가",
            description = "시니어(본인)의 UUID와 보호자 정보를 입력받아, 해당 보호자를 본인의 모니터링 멤버로 등록합니다.")
    @PostMapping("/{seniorUuid}/care/members")
    @PreAuthorize("hasAuthority('ROLE_SENIOR')")
    public ResponseEntity<ApiResponse<Object>> addCareMember(
            @PathVariable String seniorUuid,
            @NonNull Authentication authentication,
            @RequestBody @Valid CareRequestDTO.LinkMember requestDto) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !authentication.getName().equals(seniorUuid)) {
            return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "본인만 자신의 보호자를 추가할 수 있습니다");
        }
        CareLinkOutput<SeniorResponseDTO.Profile> result =
                careService.linkMemberToSenior(seniorUuid, requestDto);
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
