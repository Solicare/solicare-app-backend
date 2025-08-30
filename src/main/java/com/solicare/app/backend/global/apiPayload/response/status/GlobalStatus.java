package com.solicare.app.backend.global.apiPayload.response.status;

import com.solicare.app.backend.global.apiPayload.response.BaseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalStatus implements BaseCode {

    /* ==================================
     * Success Status (2xx)
     * ================================== */
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "요청을 성공적으로 처리했으며, 리소스가 생성되었습니다."),

    /* ==================================
     * Error Status (4xx, 5xx)
     * ================================== */
    // Common Error
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 리소스를 찾을 수 없습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON400A", "유효성 검증 실패"),


    // Member Error
    _DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER400", "이미 가입된 회원입니다."),
    _INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "MEMBER401", "아이디 또는 비밀번호가 올바르지 않습니다."),
    _MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "해당 회원을 찾을 수 없습니다.");


    /* ---------- 필드 ---------- */
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    /* ---------- 생성자 ---------- */
    GlobalStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}