package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.fcm.FcmDeviceRegisterRequestDTO;
import com.solicare.app.backend.application.dto.fcm.FcmDeviceUnregisterRequestDTO;
import com.solicare.app.backend.application.dto.fcm.FcmSendRequestDTO;
import com.solicare.app.backend.domain.dto.FcmSendResult;
import com.solicare.app.backend.domain.service.FcmDeviceService;
import com.solicare.app.backend.domain.service.FcmPushService;
import com.solicare.app.backend.global.apiPayload.ApiResponse;
import com.solicare.app.backend.global.apiPayload.response.status.ErrorStatus;
import com.solicare.app.backend.global.apiPayload.response.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmController {
    private final FcmPushService fcmPushService;
    private final FcmDeviceService fcmDeviceService;

    @PostMapping("/push")
    public ResponseEntity<ApiResponse<FcmSendResult>> pushMessage(
            @NonNull Authentication authentication,
            @RequestBody @Valid FcmSendRequestDTO fcmSendRequestDTO
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        FcmSendResult result = fcmPushService.sendMessageToMember(jwt.getClaimAsString("uuid"), fcmSendRequestDTO.getTitle(), fcmSendRequestDTO.getBody());
        if (result.getStatus() == FcmSendResult.Status.SENT) {
            return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result));
        }
        return ResponseEntity.ok(ApiResponse.onFailure(ErrorStatus._INTERNAL_SERVER_ERROR.getCode(), "FCM발송중 오류가 발생했습니다", result));
    }

    @PutMapping("/register/{token}") // 등록
    public ResponseEntity<ApiResponse<Void>> registerToken(
            Authentication authentication,
            @PathVariable String token,
            @RequestBody @Valid FcmDeviceRegisterRequestDTO req
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }

    @DeleteMapping("/unregister/{token}") // 등록 해제
    public ResponseEntity<ApiResponse<Void>> unregisterToken(
            Authentication authentication,
            @PathVariable String token,
            @RequestBody @Valid FcmDeviceUnregisterRequestDTO req
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }
}