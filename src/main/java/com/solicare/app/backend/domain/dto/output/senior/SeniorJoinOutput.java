// SeniorJoinOutput.java
package com.solicare.app.backend.domain.dto.output.senior;

import com.solicare.app.backend.application.dto.res.SeniorAuthResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorJoinOutput implements OperationOutput {
    private Status status;
    private SeniorAuthResponseDTO.Join response;
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