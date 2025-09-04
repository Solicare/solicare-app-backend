// SeniorJoinOutput.java
package com.solicare.app.backend.domain.dto.senior;

import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorCreateResult implements ServiceResult {
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
