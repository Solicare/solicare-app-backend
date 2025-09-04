package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.PushRequestDTO;
import com.solicare.app.backend.domain.dto.device.DeviceManageResult;
import com.solicare.app.backend.domain.dto.push.PushDeliveryResult;
import com.solicare.app.backend.domain.enums.Push;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.service.DeviceService;
import com.solicare.app.backend.domain.service.FirebaseService;
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

@Tag(name = "Firebase", description = "Google Firebase 관련 API")
@RestController
@RequestMapping("/api/firebase")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FirebaseController {
    private final DeviceService deviceService;
    private final FirebaseService firebaseService;
    private final ApiResponseFactory apiResponseFactory;

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
    public ResponseEntity<ApiResponse<Object>> fcmPush(
            @NonNull Authentication authentication,
            @PathVariable String token,
            @Schema(name = "FcmSendRequest", description = "FCM 푸시 요청 DTO") @RequestBody @Valid
                    PushRequestDTO.Send fcmSendRequestDTO) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            PushDeliveryResult detail =
                    firebaseService.sendMessageTo(
                            token, fcmSendRequestDTO.title(), fcmSendRequestDTO.body());
            return apiResponseFactory.onSuccess(detail);
        }
        Role role =
                Role.valueOf(
                        authentication.getAuthorities().stream()
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("No role found"))
                                .getAuthority()
                                .replace("ROLE_", ""));
        if (!deviceService.isDeviceOwner(role, authentication.getName(), Push.FCM, token)) {
            return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "본인의 기기만 전송 가능합니다.");
        }
        PushDeliveryResult detail =
                firebaseService.sendMessageTo(
                        token, fcmSendRequestDTO.title(), fcmSendRequestDTO.body());
        return apiResponseFactory.onSuccess(detail);
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
    public ResponseEntity<ApiResponse<Object>> fcmRegister(@PathVariable String token) {
        DeviceManageResult result = deviceService.create(Push.FCM, token);
        return apiResponseFactory.onSuccess(result);
    }

    @DeleteMapping("/fcm/{token}")
    @Operation(summary = "FCM 토큰 해제", description = "FCM 토큰을 해제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 해제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "자격 증명 실패")
    })
    public ResponseEntity<ApiResponse<Object>> fcmUnregister(@PathVariable String token) {
        DeviceManageResult result = deviceService.delete(Push.FCM, token);
        return apiResponseFactory.onSuccess(result);
    }
}
