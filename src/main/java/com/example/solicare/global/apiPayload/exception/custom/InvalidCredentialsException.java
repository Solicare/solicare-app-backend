package com.example.solicare.global.apiPayload.exception.custom;

import com.example.solicare.global.apiPayload.exception.GeneralException;
import com.example.solicare.global.apiPayload.code.status.ErrorStatus;

public class InvalidCredentialsException extends GeneralException {
    public InvalidCredentialsException() {
        super(ErrorStatus._INVALID_CREDENTIALS);
    }
}
