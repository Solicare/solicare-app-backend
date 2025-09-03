package com.solicare.app.backend.domain.dto.output.device;

import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DeviceManageOutput implements OperationOutput {
    Status status;
    DeviceResponseDTO.Info response;
    Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.ENABLED
                || status == Status.CREATED
                || status == Status.DELETED
                || status == Status.LINKED
                || status == Status.UNLINKED;
    }

    public enum Status {
        ENABLED,
        CREATED,
        DELETED,
        ALREADY_EXISTS,
        LINKED,
        UNLINKED,
        DEVICE_NOT_FOUND,
        MEMBER_NOT_FOUND,
        SENIOR_NOT_FOUND,
        ERROR
    }
}
