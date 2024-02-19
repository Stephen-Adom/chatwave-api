package com.chats.chatwave.ExceptionHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.TokenExpiredException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.ErrorMessage;
import com.chats.chatwave.model.ValidationError;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class GlobalExceptionHander {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(Exception exception) {
        System.out.println("============================= from global exception handler ===========================");
        System.out.println(exception.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Unknown internal server error.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<Map<String, Object>>(response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(exception.getMessage());
        message.setStatus(exception.getStatus());

        return new ResponseEntity<ErrorMessage>(message, exception.getStatus());
    }

    @ExceptionHandler(ValidationErrorsException.class)
    public ResponseEntity<ValidationError> handleValidationError(ValidationErrorsException exception) {
        List<String> messages = new ArrayList<>();

        messages.addAll(exception.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList()));

        ValidationError error = ValidationError.builder().messages(messages).status(HttpStatus.UNPROCESSABLE_ENTITY)
                .build();

        return new ResponseEntity<ValidationError>(error, HttpStatus.UNAUTHORIZED);
    }
}
