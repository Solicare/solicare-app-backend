package com.solicare.app.backend.global.apiPayload.response;

import org.springframework.http.HttpStatus;

public interface BaseCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
