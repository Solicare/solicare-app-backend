package com.solicare.app.backend.domain.dto.member;

import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MemberLoginResult implements ServiceResult {
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
