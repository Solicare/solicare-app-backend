package com.solicare.app.backend.global.exception;

import com.solicare.app.backend.global.res.ApiStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ApiStatus status;

    public ApiException(ApiStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
