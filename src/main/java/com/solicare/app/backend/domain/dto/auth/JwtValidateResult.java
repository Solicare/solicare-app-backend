package com.solicare.app.backend.domain.dto.auth;

import com.solicare.app.backend.domain.dto.ServiceResult;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class JwtValidateResult implements ServiceResult {
    private Status status;
    private Jws<Claims> jwsClaims;
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
