package com.example.solicare.application.dto.fcm;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenUnregisterRequestDTO {

    @NotBlank
    private String token;

    @Builder
    public FcmTokenUnregisterRequestDTO(String token) {
        this.token = token;
    }
}
