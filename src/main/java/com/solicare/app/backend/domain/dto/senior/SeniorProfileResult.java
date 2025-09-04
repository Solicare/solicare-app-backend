package com.solicare.app.backend.domain.dto.senior;

import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorProfileResult implements ServiceResult {
    private Status status;
    private SeniorResponseDTO.Profile response;
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
