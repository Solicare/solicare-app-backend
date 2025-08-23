package com.solicare.app.backend.application.dto.fcm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmDeviceUnregisterRequestDTO {
    private String token;

    @Builder
    public FcmDeviceUnregisterRequestDTO(String token) {
        this.token = token;
    }
}
