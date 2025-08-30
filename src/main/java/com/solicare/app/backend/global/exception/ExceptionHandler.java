package com.solicare.app.backend.global.exception;

import com.google.api.gax.rpc.NotFoundException;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiResponseFactory;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionHandler {
    private final ApiResponseFactory apiResponseFactory;

    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Exception>> handleCustomException(ApiException e) {
        return apiResponseFactory.onError(e.getStatus(), e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Exception>> handleNotFoundException(NotFoundException e) {
        return apiResponseFactory.onError(ApiStatus._NOT_FOUND, e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Exception>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e) {
        // TODO: Extract detailed validation error messages if needed, respond via onFailure()
        return apiResponseFactory.onError(ApiStatus._INVALID_FIELD, e);
    }

    /** 기타 모든 예외 처리 (최후의 보루) */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Exception>> handleException(Exception e) {
        log.error("Unhandled Exception occurred: {}", e.getMessage(), e);
        return apiResponseFactory.onError(ApiStatus._INTERNAL_SERVER_ERROR, e);
    }
}
