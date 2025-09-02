package com.solicare.app.backend.domain.dto.output.senior;

import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorProfileOutput implements OperationOutput {
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
