package com.solicare.app.backend.domain.dto.output.member;

import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MemberJoinOutput implements OperationOutput {
    private Status status;
    private MemberResponseDTO.Join response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        USER_ALREADY_EXISTS,
        ERROR
    }
}
