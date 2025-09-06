package com.solicare.app.backend.domain.dto.member;

import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.ServiceResult;
import com.solicare.app.backend.global.res.ApiStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MemberProfileResult implements ServiceResult {
    private Status status;
    private MemberResponseDTO.Profile response;
    private Exception exception;

    @Override
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SUCCESS(ApiStatus._OK, "MEMBER200", "멤버 프로필 조회가 성공적으로 처리되었습니다."),
        NOT_FOUND(ApiStatus._NOT_FOUND, "MEMBER404", "해당 멤버를 찾을 수 없습니다."),
        ERROR(ApiStatus._INTERNAL_SERVER_ERROR, "MEMBER500", "멤버 프로필 조회 중 오류가 발생했습니다.");

        private final ApiStatus apiStatus;
        private final String code;
        private final String message;
    }
}
