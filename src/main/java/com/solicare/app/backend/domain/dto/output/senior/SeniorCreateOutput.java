// SeniorJoinOutput.java
package com.solicare.app.backend.domain.dto.output.senior;

import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorCreateOutput implements OperationOutput {
    private Status status;
    private SeniorResponseDTO.Create response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        SENIOR_ALREADY_EXISTS,
        ERROR
    }
}
