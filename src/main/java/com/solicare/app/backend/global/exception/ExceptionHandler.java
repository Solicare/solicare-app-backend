package com.solicare.app.backend.global.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.api.gax.rpc.NotFoundException;
import com.solicare.app.backend.application.factory.ApiResponseFactory;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiStatus;

import jakarta.validation.ConstraintViolationException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionHandler {
    private final ApiResponseFactory apiResponseFactory;

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException e) {
        List<String> errorMessages =
                e.getConstraintViolations().stream()
                        .map(
                                violation ->
                                        String.format(
                                                "'%s' 필드: %s (입력값: %s)",
                                                violation.getPropertyPath().toString(),
                                                violation.getMessage(),
                                                violation.getInvalidValue()))
                        .collect(Collectors.toList());
        return apiResponseFactory.onError(ApiStatus._BAD_REQUEST, errorMessages);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException e) {
        return apiResponseFactory.onError(ApiStatus._BAD_REQUEST, List.of(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e) {
        List<String> errorMessages =
                e.getBindingResult().getFieldErrors().stream()
                        .map(
                                fieldError ->
                                        String.format(
                                                "'%s' 필드: %s (입력값: %s)",
                                                fieldError.getField(),
                                                fieldError.getDefaultMessage(),
                                                fieldError.getRejectedValue()))
                        .collect(Collectors.toList());
        return apiResponseFactory.onError(ApiStatus._BAD_REQUEST, errorMessages);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ie) {
            List<String> errorMessages =
                    ie.getPath().stream()
                            .map(JsonMappingException.Reference::getFieldName)
                            .map(
                                    fieldName ->
                                            String.format(
                                                    "'%s' 필드에 잘못된 값이 입력되었습니다. (입력값: %s)",
                                                    fieldName, ie.getValue().toString()))
                            .collect(Collectors.toList());

            return apiResponseFactory.onError(ApiStatus._BAD_REQUEST, errorMessages);
        }
        return apiResponseFactory.onError(ApiStatus._BAD_REQUEST, List.of(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(
            org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(
            org.springframework.web.servlet.NoHandlerFoundException e) {
        return apiResponseFactory.onError(ApiStatus._NOT_FOUND, List.of(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException e) {
        return apiResponseFactory.onError(ApiStatus._NOT_FOUND, List.of(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(
            org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(
            org.springframework.web.HttpRequestMethodNotSupportedException e) {
        return apiResponseFactory.onError(ApiStatus._METHOD_NOT_ALLOWED, List.of(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return apiResponseFactory.onError(
                ApiStatus._INTERNAL_SERVER_ERROR, List.of(e.getMessage()));
    }
}
