package com.example.solicare.global.apiPayload.response.status;

import com.example.solicare.global.apiPayload.response.BaseErrorCode;
import com.example.solicare.global.apiPayload.response.ErrorReasonDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus implements BaseErrorCode {

    /* ===== 공통 ===== */
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    /* ===== 회원 도메인 ===== */
    _DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER400", "이미 가입된 회원입니다."),
    _INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "MEMBER401", "아이디 또는 비밀번호가 올바르지 않습니다."),
    _MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "해당 회원을 찾을 수 없습니다."),

    /* ===== 검증/리소스 ===== */
    VALIDATION_ERROR("COMMON400A", "유효성 검증 실패"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "리소스를 찾을 수 없습니다.");

    /* ---------- 필드 ---------- */
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    /* ---------- 생성자 ---------- */
    // 3-파라미터 : HttpStatus 직접 지정
    ErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    // 2-파라미터 : BAD_REQUEST 기본
    ErrorStatus(String code, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.message = message;
    }

    /* ---------- 인터페이스 구현 ---------- */
    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}