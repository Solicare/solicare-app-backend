package com.solicare.app.backend.domain.dto.push;

import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class PushDeliveryResult implements ServiceResult {
    private Status status;
    private Exception exception;

    public boolean isSuccess() {
        return status == Status.SENT;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SENT(ApiStatus._OK, "PUSH200", "푸시 알림 전송 성공"),
        UNAVAILABLE(ApiStatus._BAD_REQUEST, "PUSH400", "푸시 알림 서비스 사용 불가"),
        ERROR(ApiStatus._INTERNAL_SERVER_ERROR, "PUSH500", "푸시 알림 전송 실패");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
