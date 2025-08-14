package com.example.solicare.global.apiPayload.exception.custom;

import com.example.solicare.global.apiPayload.code.status.ErrorStatus;
import com.example.solicare.global.apiPayload.exception.GeneralException;

public class MemberNotFoundException extends GeneralException {
    public MemberNotFoundException() {
        super(ErrorStatus._MEMBER_NOT_FOUND);
    }
}
