package com.solicare.app.backend.global.apiPayload.exception;

import com.solicare.app.backend.global.apiPayload.response.status.GlobalStatus;

public interface BaseException {

    GlobalStatus getStatus();
}
