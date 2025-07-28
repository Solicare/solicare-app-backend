package com.example.solicare.global.apiPayload.exception.custom;


import com.example.solicare.global.apiPayload.exception.GeneralException;
import com.example.solicare.global.apiPayload.code.status.ErrorStatus;

public class DuplicateMemberException extends GeneralException {
    public DuplicateMemberException() {
        super(ErrorStatus._DUPLICATE_MEMBER);
    }
}
