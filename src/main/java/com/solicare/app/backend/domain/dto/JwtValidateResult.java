package com.solicare.app.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO: implement OperationResult interface
@Getter
@AllArgsConstructor(staticName = "of")
public class JwtValidateResult {
    private Status status;

    public enum Status {
        VALID, INVALID, EXPIRED
    }
}