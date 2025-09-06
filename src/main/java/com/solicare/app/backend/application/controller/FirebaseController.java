package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.PushRequestDTO;
import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.application.factory.ApiResponseFactory;
import com.solicare.app.backend.domain.dto.device.DeviceManageResult;
import com.solicare.app.backend.domain.dto.device.DeviceQueryResult;
import com.solicare.app.backend.domain.dto.push.PushDeliveryResult;
import com.solicare.app.backend.domain.enums.Push;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.service.DeviceService;
import com.solicare.app.backend.domain.service.FirebaseService;
import com.solicare.app.backend.global.res.ApiResponse;
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

import java.util.List;

@Tag(name = "Firebase", description = "Google Firebase 관련 API")
@RestController
@RequestMapping("/api/firebase")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FirebaseController {
    private final DeviceService deviceService;
    private final FirebaseService firebaseService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/fcm/status")
    @Operation(summary = "FCM 상태 확인", description = "FCM 토큰의 등록 상태를 확인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "상태 확인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    @PreAuthorize("hasAnyRole('SENIOR', 'MEMBER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<DeviceResponseDTO.Info>>> fcmStatus(
            Authentication authentication, @RequestParam String token) {
        if (authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Role role =
                    Role.valueOf(
                            authentication.getAuthorities().stream()
                                    .findFirst()
                                    .orElseThrow(
                                            () -> new IllegalArgumentException("No role found"))
                                    .getAuthority()
                                    .replace("ROLE_", ""));
            if (!deviceService.isDeviceOwner(role, authentication.getName(), Push.FCM, token)) {
                return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "본인의 기기만 조회 가능합니다.");
            }
        }
        DeviceQueryResult result = deviceService.getCurrentStatus(Push.FCM, token);
        return apiResponseFactory.onResult(
                result.getStatus().getApiStatus(),
                result.getStatus().getCode(),
                result.getStatus().getMessage(),
                result.getResponse(),
                result.getException());
    }

    @PutMapping("/fcm/{token}")
    @Operation(summary = "FCM 토큰 등록", description = "FCM 토큰을 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "기기 등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    public ResponseEntity<ApiResponse<DeviceResponseDTO.Info>> fcmRegister(
            @PathVariable String token) {
        DeviceManageResult result = deviceService.register(Push.FCM, token);
        return apiResponseFactory.onResult(
                result.getStatus().getApiStatus(),
                result.getStatus().getCode(),
                result.getStatus().getMessage(),
                result.getResponse(),
                result.getException());
    }

    @PostMapping("/fcm/{token}")
    @Operation(summary = "FCM 푸시 전송", description = "특정 토큰으로 FCM 푸시를 전송합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "푸시 전송 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    @PreAuthorize("hasAnyRole('SENIOR', 'MEMBER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> fcmPush(
            @NonNull Authentication authentication,
            @PathVariable String token,
            @RequestBody @Valid PushRequestDTO.Send dto) {
        if (authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Role role =
                    Role.valueOf(
                            authentication.getAuthorities().stream()
                                    .findFirst()
                                    .orElseThrow(
                                            () -> new IllegalArgumentException("No role found"))
                                    .getAuthority()
                                    .replace("ROLE_", ""));
            if (!deviceService.isDeviceOwner(role, authentication.getName(), Push.FCM, token)) {
                return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "본인의 기기만 전송 가능합니다.");
            }
        }
        PushDeliveryResult result =
                firebaseService.sendMessageTo(token, dto.channel(), dto.title(), dto.message());
        return apiResponseFactory.onResult(
                result.getStatus().getApiStatus(),
                result.getStatus().getCode(),
                result.getStatus().getMessage(),
                null,
                result.getException());
    }

    @DeleteMapping("/fcm/{token}")
    @Operation(summary = "FCM 토큰 삭제", description = "특정 FCM 토큰을 DB에서 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 해제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    public ResponseEntity<ApiResponse<DeviceResponseDTO.Info>> fcmUnregister(
            @PathVariable String token) {
        DeviceManageResult result = deviceService.delete(Push.FCM, token);
        return apiResponseFactory.onResult(
                result.getStatus().getApiStatus(),
                result.getStatus().getCode(),
                result.getStatus().getMessage(),
                result.getResponse(),
                result.getException());
    }

    @PostMapping("/fcm/renew")
    @Operation(
            summary = "FCM 토큰 갱신",
            description = "기존 FCM 토큰을 새로운 토큰으로 업데이트 합니다. 기존에 등록된 토큰이 없으면 새로 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "토큰 업데이트/등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    public ResponseEntity<ApiResponse<DeviceResponseDTO.Info>> fcmRenew(
            @RequestBody @Valid PushRequestDTO.RenewToken dto) {
        DeviceManageResult result = deviceService.update(Push.FCM, dto.oldToken(), dto.newToken());
        return apiResponseFactory.onResult(
                result.getStatus().getApiStatus(),
                result.getStatus().getCode(),
                result.getStatus().getMessage(),
                result.getResponse(),
                result.getException());
    }
}
