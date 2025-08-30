// SeniorLoginOutput.java
package com.solicare.app.backend.domain.dto.output.senior;

import com.solicare.app.backend.application.dto.res.SeniorAuthResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorLoginOutput implements OperationOutput {
    private Status status;
    private SeniorAuthResponseDTO.Login response;
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