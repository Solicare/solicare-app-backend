package com.solicare.app.backend.domain.dto.output.member;

import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class MemberInfoOutput implements OperationOutput {
    private Status status;
    private MemberResponseDTO.Login response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        USER_NOT_FOUND,
        INVALID_PASSWORD,
        ERROR
    }
}
