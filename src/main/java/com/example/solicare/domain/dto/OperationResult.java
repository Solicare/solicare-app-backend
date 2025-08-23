package com.example.solicare.domain.dto;

public interface OperationResult<D extends OperationResult.Body<?>, E extends Exception> {
    boolean isSuccess();

    D getBody();

    E getException();

    class Body<T> {
        T status;
    }
}
