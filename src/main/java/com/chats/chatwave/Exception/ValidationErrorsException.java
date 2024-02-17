package com.chats.chatwave.Exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public class ValidationErrorsException extends Exception {
    private HttpStatus status;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationErrorsException(List<FieldError> fieldErrors, HttpStatus status) {
        super();
        this.status = status;
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return this.fieldErrors;
    }

    public HttpStatus httpStatus() {
        return this.status;
    }
}
