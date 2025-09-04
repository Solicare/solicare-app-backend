package com.solicare.app.backend.domain.dto.member;

import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MemberProfileResult implements ServiceResult {
    private Status status;
    private MemberResponseDTO.Profile response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        NOT_FOUND,
        ERROR
    }
}
