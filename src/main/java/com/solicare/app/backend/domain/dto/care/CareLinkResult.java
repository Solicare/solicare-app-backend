package com.solicare.app.backend.domain.dto.care;

import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CareLinkResult<ProfileResponse> implements ServiceResult {
    private Status status;
    private ProfileResponse response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SUCCESS(ApiStatus._OK, "CARE200", "연결이 성공적으로 처리되었습니다."),
        ALREADY_LINKED(ApiStatus._CONFLICT, "CARE409", "이미 연결된 멤버와 시니어입니다."),
        MEMBER_NOT_FOUND(ApiStatus._NOT_FOUND, "MEMBER404", "멤버를 찾을 수 없습니다."),
        INVALID_MEMBER_PASSWORD(ApiStatus._BAD_REQUEST, "MEMBER400", "멤버 비밀번호가 올바르지 않습니다."),
        SENIOR_NOT_FOUND(ApiStatus._NOT_FOUND, "SENIOR404", "시니어를 찾을 수 없습니다."),
        INVALID_SENIOR_PASSWORD(ApiStatus._BAD_REQUEST, "SENIOR400", "시니어 비밀번호가 올바르지 않습니다."),
        ERROR(ApiStatus._INTERNAL_SERVER_ERROR, "CARE500", "연결 처리 중 오류가 발생했습니다.");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
