package com.solicare.app.backend.domain.dto.care;

import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class CareQueryResult<ProfileResponse> implements ServiceResult {
    private Status status;
    private List<ProfileResponse> response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        MEMBER_NOT_FOUND,
        SENIOR_NOT_FOUND,
        ERROR
    }
}
