package com.solicare.app.backend.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.solicare.app.backend.global.apiPayload.response.status.GlobalStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 1. 비즈니스 성공 응답
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, GlobalStatus._OK.getCode(), GlobalStatus._OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> onSuccess() {
        return new ApiResponse<>(true, GlobalStatus._OK.getCode(), GlobalStatus._OK.getMessage(), null);
    }


    // 2. 비즈니스 실패 응답 (Enum 사용)
    public static <T> ApiResponse<T> onFailure(GlobalStatus status, T result) {
        return new ApiResponse<>(false, status.getCode(), status.getMessage(), result);
    }

    public static <T> ApiResponse<T> onFailure(GlobalStatus status) {
        return new ApiResponse<>(false, status.getCode(), status.getMessage(), null);
    }


    /**
     * 2-1. 비즈니스 실패 응답 (동적 메시지 사용)
     * @Valid 예외와 같이 동적인 메시지가 필요한 경우를 위해 사용됩니다.
     * @param code 응답 코드 (String)
     * @param message 응답 메시지 (String)
     * @param result 실패 관련 데이터 (선택 사항)
     */
    public static <T> ApiResponse<T> onFailure(String code, String message, T result) {
        return new ApiResponse<>(false, code, message, result);
    }


    // 3. 예외(오류) 발생 시 응답
    public static <T> ApiResponse<T> onError(GlobalStatus status) {
        return new ApiResponse<>(false, status.getCode(), status.getMessage(), null);
    }
}