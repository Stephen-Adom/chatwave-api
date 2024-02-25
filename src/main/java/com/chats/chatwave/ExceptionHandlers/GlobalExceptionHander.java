package com.chats.chatwave.ExceptionHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.ErrorMessage;
import com.chats.chatwave.model.ValidationError;

@ControllerAdvice
public class GlobalExceptionHander {

    @SuppressWarnings("null")
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(exception.getMessage());
        message.setStatus(exception.getStatus());

        return new ResponseEntity<ErrorMessage>(message, exception.getStatus());
    }

    @SuppressWarnings("null")
    @ExceptionHandler(ValidationErrorsException.class)
    public ResponseEntity<ValidationError> handleValidationError(ValidationErrorsException exception) {
        List<String> messages = new ArrayList<>();

        messages.addAll(exception.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList()));

        ValidationError error = ValidationError.builder().messages(messages).status(HttpStatus.UNPROCESSABLE_ENTITY)
                .build();

        return new ResponseEntity<ValidationError>(error, exception.httpStatus());
    }
}
