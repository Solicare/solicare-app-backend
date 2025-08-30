package com.solicare.app.backend.domain.dto.output.care;

import com.solicare.app.backend.application.dto.res.CareRelationResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CareLinkOutput implements OperationOutput {
    private Status status;
    private CareRelationResponseDTO.Link response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public enum Status {
        SUCCESS,
        MEMBER_NOT_FOUND,
        SENIOR_NOT_FOUND,
        INVALID_SENIOR_PASSWORD,
        RELATION_ALREADY_EXISTS,
        ERROR
    }
}