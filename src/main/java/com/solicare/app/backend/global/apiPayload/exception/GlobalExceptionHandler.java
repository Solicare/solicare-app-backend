package com.solicare.app.backend.global.apiPayload.exception;

import com.solicare.app.backend.global.apiPayload.ApiResponse;
import com.solicare.app.backend.global.apiPayload.response.status.GlobalStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 우리가 직접 정의한 CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        log.warn("CustomException occurred: {}", e.getMessage(), e);
        GlobalStatus status = e.getStatus();
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(ApiResponse.onError(status));
    }

    /**
     * @Valid 어노테이션을 사용한 DTO의 유효성 검증 실패 시 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException occurred: {}", e.getMessage());
        GlobalStatus status = GlobalStatus.VALIDATION_ERROR;
        // 첫 번째 에러 메시지를 응답으로 사용
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 💥 수정된 부분: new ApiResponse<>() 대신 정적 팩토리 메서드 사용
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(ApiResponse.onFailure(status.getCode(), errorMessage, null));
    }


    /**
     * 기타 모든 예외 처리 (최후의 보루)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled Exception occurred: {}", e.getMessage(), e);
        GlobalStatus status = GlobalStatus._INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(ApiResponse.onError(status));
    }
}