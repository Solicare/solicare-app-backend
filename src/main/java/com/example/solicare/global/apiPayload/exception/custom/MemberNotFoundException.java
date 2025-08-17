package com.example.solicare.global.apiPayload.exception.custom;

import com.example.solicare.global.apiPayload.exception.GeneralException;
import com.example.solicare.global.apiPayload.response.status.ErrorStatus;

public class MemberNotFoundException extends GeneralException {
    public MemberNotFoundException() {
        super(ErrorStatus._MEMBER_NOT_FOUND);
    }
}
