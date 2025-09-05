package com.solicare.app.backend.domain.dto.device;

import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DeviceManageResult implements ServiceResult {
    Status status;
    DeviceResponseDTO.Info response;
    Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.ENABLED
                || status == Status.CREATED
                || status == Status.UPDATED
                || status == Status.DELETED
                || status == Status.LINKED
                || status == Status.UNLINKED;
    }

    public enum Status {
        ENABLED,
        CREATED,
        UPDATED,
        DELETED,
        ALREADY_EXISTS,
        LINKED,
        UNLINKED,
        ALREADY_LINKED,
        DEVICE_NOT_FOUND,
        MEMBER_NOT_FOUND,
        SENIOR_NOT_FOUND,
        ERROR
    }
}
