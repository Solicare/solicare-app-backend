package com.solicare.app.backend.domain.dto.output.device;

import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class DeviceQueryOutput implements OperationOutput {
    Status status;
    List<DeviceResponseDTO.Info> response;
    Exception exception;

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
