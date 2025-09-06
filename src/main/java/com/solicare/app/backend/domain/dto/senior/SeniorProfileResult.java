package com.solicare.app.backend.domain.dto.senior;

import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SeniorProfileResult implements ServiceResult {
    private Status status;
    private SeniorResponseDTO.Profile response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SUCCESS(ApiStatus._OK, "SENIOR200", "시니어 프로필 조회가 성공적으로 처리되었습니다."),
        NOT_FOUND(ApiStatus._NOT_FOUND, "SENIOR404", "해당 시니어를 찾을 수 없습니다."),
        ERROR(ApiStatus._INTERNAL_SERVER_ERROR, "SENIOR500", "시니어 프로필 조회 중 오류가 발생했습니다.");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
