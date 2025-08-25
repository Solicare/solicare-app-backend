package com.solicare.app.backend.domain.dto.output.auth;

import com.solicare.app.backend.domain.dto.output.OperationOutput;

import io.jsonwebtoken.Claims;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class JwtValidateOutput implements OperationOutput {
    private Status status;
    private Claims claims;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.VALID;
    }

    public enum Status {
        VALID,
        INVALID,
        EXPIRED
    }
}
