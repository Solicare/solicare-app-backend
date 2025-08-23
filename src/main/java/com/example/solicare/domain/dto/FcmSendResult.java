package com.example.solicare.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO: implement OperationResult interface
@Getter
@AllArgsConstructor(staticName = "of")
public class FcmSendResult {
    private Status status;
    private Exception exception;

    public enum Status {
        SENT, MEMBER_NOT_FOUND, NOT_REGISTER, ERROR
    }
}