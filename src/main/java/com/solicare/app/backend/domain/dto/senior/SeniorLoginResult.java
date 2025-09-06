// SeniorLoginOutput.java
package com.solicare.app.backend.domain.dto.senior;

import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorLoginResult implements ServiceResult {
    private Status status;
    private SeniorResponseDTO.Login response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SUCCESS(ApiStatus._OK, "SENIOR200", "시니어 로그인에 성공했습니다."),
        SENIOR_NOT_FOUND(ApiStatus._NOT_FOUND, "SENIOR404", "존재하지 않는 시니어 아이디입니다."),
        INVALID_PASSWORD(ApiStatus._BAD_REQUEST, "SENIOR400", "비밀번호가 올바르지 않습니다."),
        ERROR(ApiStatus._INTERNAL_SERVER_ERROR, "SENIOR500", "시니어 로그인 처리 중 오류가 발생했습니다.");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
