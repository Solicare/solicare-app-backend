package com.solicare.app.backend.domain.dto.output.care;

import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CareLinkOutput<ProfileResponse> implements OperationOutput {
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
