package com.solicare.app.backend.application.factory;

import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseFactory {
    public ResponseEntity<ApiResponse<Void>> onStatus(ApiStatus status) {
        return onCustom(
                status.getHttpStatus(),
                status == ApiStatus._OK || status == ApiStatus._CREATED,
                status.getCode(),
                status.getMessage(),
                null,
                null);
    }

    private <T> ResponseEntity<ApiResponse<T>> onCustom(
            HttpStatus httpStatus,
            boolean isSuccess,
            String code,
            String message,
            T body,
            List<String> errors) {
        return ResponseEntity.status(httpStatus)
                .body(new ApiResponse<>(isSuccess, code, message, body, errors));
    }

    public <T> ResponseEntity<ApiResponse<T>> onResult(
            ApiStatus status, String code, String message, T body, Exception exception) {
        return onCustom(
                status.getHttpStatus(),
                status == ApiStatus._OK || status == ApiStatus._CREATED,
                code,
                message,
                body,
                exception == null ? null : List.of(exception.getMessage()));
    }

    public <T> ResponseEntity<ApiResponse<T>> onFailure(ApiStatus status, String message) {
        return onCustom(status.getHttpStatus(), false, status.getCode(), message, null, null);
    }

    public ResponseEntity<ApiResponse<Void>> onError(ApiStatus status, List<String> errors) {
        return onCustom(
                status.getHttpStatus(), false, status.getCode(), status.getMessage(), null, errors);
    }
}
