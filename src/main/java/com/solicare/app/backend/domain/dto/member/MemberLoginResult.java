package com.solicare.app.backend.domain.dto.member;

import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MemberLoginResult implements ServiceResult {
    private Status status;
    private MemberResponseDTO.Login response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SUCCESS(ApiStatus._OK, "MEMBER200", "로그인에 성공했습니다."),
        USER_NOT_FOUND(ApiStatus._NOT_FOUND, "MEMBER404", "사용자를 찾을 수 없습니다."),
        INVALID_PASSWORD(ApiStatus._BAD_REQUEST, "MEMBER401", "비밀번호가 올바르지 않습니다."),
        ERROR(ApiStatus._INTERNAL_SERVER_ERROR, "MEMBER500", "멤버 로그인 중 오류가 발생했습니다.");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
