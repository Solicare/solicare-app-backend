package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.FcmRequestDTO;
import com.solicare.app.backend.domain.service.PushService;
import com.solicare.app.backend.global.apiPayload.ApiResponse;
import com.solicare.app.backend.global.apiPayload.response.status.ErrorStatus;
import com.solicare.app.backend.global.apiPayload.response.status.SuccessStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Push-FCM", description = "FCM푸시 관련 API")
@RestController
@RequestMapping("/api/push/fcm")
@RequiredArgsConstructor
public class PushFcmController {
    private final PushService pushService;

    @PostMapping("/push")
    public ResponseEntity<ApiResponse<Void>> pushMessage(
            @NonNull Authentication authentication,
            @Schema(name = "FcmSendRequest", description = "FCM 푸시 알림 요청 DTO") @RequestBody @Valid
                    FcmRequestDTO.Send fcmSendRequestDTO) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        //    FcmSendOutputDetail result =
        //        pushService.sendFcmMessageToMemberByUuid(
        //            jwt.getClaimAsString("uuid"), fcmSendRequestDTO.title(),
        // fcmSendRequestDTO.body());
        //    if (result.getStatus() == FcmSendOutputDetail.Status.SENT) {
        //      return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result));
        return ResponseEntity.ok(
                ApiResponse.onFailure(
                        ErrorStatus._INTERNAL_SERVER_ERROR.getCode(), "FCM발송중 오류가 발생했습니다", null));
    }

    @PutMapping("/register/{token}") // 등록
    public ResponseEntity<ApiResponse<Void>> registerToken(
            Authentication authentication,
            @PathVariable String token,
            @Schema(name = "FcmRegisterRequest", description = "FCM 디바이스 등록 요청 DTO")
                    @RequestBody
                    @Valid
                    FcmRequestDTO.Register req) {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }

    @DeleteMapping("/unregister/{token}") // 등록 해제
    public ResponseEntity<ApiResponse<Void>> unregisterToken(
            Authentication authentication,
            @PathVariable String token,
            @Schema(name = "FcmDeleteRequest", description = "FCM 디바이스 삭제 요청 DTO")
                    @RequestBody
                    @Valid
                    FcmRequestDTO.Delete req) {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }

    // TODO: implement different status handling after refactoring PushService
}
