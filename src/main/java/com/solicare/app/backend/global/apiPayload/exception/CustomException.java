package com.solicare.app.backend.global.apiPayload.exception;

import com.solicare.app.backend.global.apiPayload.response.status.GlobalStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException implements BaseException {
    private final GlobalStatus status;

    public CustomException(GlobalStatus status) {
        super(status.getMessage());
        this.status = status;
    }

}
