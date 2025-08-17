package com.example.solicare.application.controller;

import com.example.solicare.application.dto.fcm.FcmMessageDTO;
import com.example.solicare.application.dto.fcm.FcmTokenRegisterRequestDTO;
import com.example.solicare.application.dto.fcm.FcmTokenUnregisterRequestDTO;
import com.example.solicare.domain.service.DeviceTokenService;
import com.example.solicare.domain.service.FcmService;
import com.example.solicare.global.apiPayload.ApiResponse;
import com.example.solicare.global.apiPayload.response.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final DeviceTokenService deviceTokenService;
    private final FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Integer>> pushMessage(@RequestBody @Valid FcmMessageDTO fcmMessageDTO) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다.");
        boolean result = fcmService.sendMessageTo(fcmMessageDTO.getToken(), fcmMessageDTO.getTitle(), fcmMessageDTO.getBody());
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result ? 1 : 0));
    }

    @PostMapping("/register") // 등록
    public ResponseEntity<ApiResponse<Void>> registerToken(
            @RequestBody @Valid FcmTokenRegisterRequestDTO req,
            Authentication authentication // 로그인 연동하려면 여기서 memberId 추출
    ) {
        deviceTokenService.register(req.getPhoneNumber(), req.getToken());
        fcmService.sendMessageTo(req.getToken(), "SoliCare Test", "푸시 알림 등록이 완료되었습니다.");
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }

    @DeleteMapping("/unregister") // 등록 해제
    public ResponseEntity<ApiResponse<Void>> unregisterToken(
            @RequestBody @Valid FcmTokenUnregisterRequestDTO req
    ) {
        deviceTokenService.unregisterByToken(req.getToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
    }
}