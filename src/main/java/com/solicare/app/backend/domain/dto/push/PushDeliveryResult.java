package com.solicare.app.backend.domain.dto.push;

import com.solicare.app.backend.domain.dto.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class PushDeliveryResult implements ServiceResult {
    private Status status;
    private Exception exception;

    public boolean isSuccess() {
        return status == Status.SENT;
    }

    public enum Status {
        SENT,
        UNAVAILABLE,
        ERROR
    }
}
