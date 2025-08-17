package com.example.solicare.application.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmMessageDTO {
    private boolean validateOnly;
    private String token;
    private String title;
    private String body;
}
