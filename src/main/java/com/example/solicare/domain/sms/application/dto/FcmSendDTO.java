package com.example.solicare.domain.sms.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendDTO {
    private String token;

    private String title;

    private String body;

    @Builder(toBuilder = true)
    public FcmSendDTO(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
