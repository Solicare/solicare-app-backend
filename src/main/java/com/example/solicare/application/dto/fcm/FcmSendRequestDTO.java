package com.example.solicare.application.dto.fcm;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmSendRequestDTO {
    private String token;
    @NotBlank
    private String title;
    @NotBlank
    private String body;
}
