package com.solicare.app.backend.global.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ApiStatus {
    /* ==================================
     * Success Status (2xx)
     * ================================== */
    _OK(HttpStatus.OK, "COMMON200", "요청이 성공적으로 처리되었습니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "요청을 성공적으로 처리했으며, 리소스가 생성되었습니다."),

    /* ==================================
     * Error Status (4xx, 5xx)
     * ================================== */
    // Common Error
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "요청이 올바르지 않습니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근이 거부되었습니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405", "허용되지 않은 요청 메서드입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "리소스를 찾을 수 없습니다."),
    _CONFLICT(HttpStatus.CONFLICT, "COMMON409", "리소스의 현재 상태와 충돌이 발생했습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "COMMON501", "지원하지 않는 기능입니다.");

    /* ---------- 필드 ---------- */
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
