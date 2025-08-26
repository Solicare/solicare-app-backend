package com.solicare.app.backend.global.apiPayload.exception;

import com.solicare.app.backend.global.apiPayload.response.BaseErrorCode;
import com.solicare.app.backend.global.apiPayload.response.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
