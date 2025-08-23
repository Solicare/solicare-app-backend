package com.example.solicare.application.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmDeviceRegisterRequestDTO {
    private String token;
    private String localNumber;
}
