package com.solicare.app.backend.global.res;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseFactory {
    public ResponseEntity<ApiResponse<Exception>> onError(ApiStatus status, Exception exception) {
        return createCustomResponse(status, status.getCode(), status.getMessage(), exception);
    }

    private <T> ResponseEntity<ApiResponse<T>> createCustomResponse(
            ApiStatus status, String code, String message, T result) {
        return ResponseEntity.status(status.getHttpStatus())
                .body(ApiResponse.of(status == ApiStatus._OK, code, message, result));
    }

    public <T> ResponseEntity<ApiResponse<T>> onSuccess(T result) {
        return createCustomResponse(
                ApiStatus._OK, ApiStatus._OK.getCode(), ApiStatus._OK.getMessage(), result);
    }

    public <T> ResponseEntity<ApiResponse<T>> onFailure(ApiStatus status, T result) {
        return createCustomResponse(status, status.getCode(), status.getMessage(), result);
    }
}
