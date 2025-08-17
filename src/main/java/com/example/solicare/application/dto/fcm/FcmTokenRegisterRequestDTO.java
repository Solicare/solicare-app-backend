package com.example.solicare.application.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FcmTokenRegisterRequestDTO {
    private String phoneNumber;
    private String token;
}
