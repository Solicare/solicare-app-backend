package com.solicare.app.backend.domain.dto.care;

import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CareLinkResult<ProfileResponse> implements ServiceResult {
    private Status status;
    private ProfileResponse response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        MEMBER_NOT_FOUND,
        SENIOR_NOT_FOUND,
        INVALID_MEMBER_PASSWORD,
        INVALID_SENIOR_PASSWORD,
        ALREADY_LINKED,
        ERROR
    }
}
