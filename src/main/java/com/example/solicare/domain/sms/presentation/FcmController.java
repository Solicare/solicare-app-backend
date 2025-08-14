package com.example.solicare.domain.sms.presentation;

import com.example.solicare.domain.sms.application.dto.FcmSendDTO;
import com.example.solicare.domain.sms.domain.FcmService;
import com.example.solicare.global.apiPayload.ApiResponse;
import com.example.solicare.global.apiPayload.code.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

    private final FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Integer>> pushMessage(@RequestBody @Valid FcmSendDTO fcmSendDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다.");
        int result = fcmService.sendMessageTo(fcmSendDto);

        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, result));
    }
}
