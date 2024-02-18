package com.chats.chatwave.ExceptionHandlers;

import java.util.HashMap;
import java.util.Map;

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

import com.chats.chatwave.Exception.TokenExpiredException;
import com.chats.chatwave.model.ErrorMessage;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHander {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(Exception exception) {

        System.out.println(
                "=============================================== global handler ================================");

        System.out.println(exception);

        if (exception instanceof BadCredentialsException) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "The username or password is incorrect");
            response.put("status", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        // if (exception instanceof AccountStatusException) {
        // errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
        // exception.getMessage());
        // errorDetail.setProperty("description", "The account is locked");
        // }

        if (exception instanceof AccessDeniedException) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "The JWT token has expired");
            response.put("status", HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        // if (exception instanceof SignatureException) {
        // errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
        // exception.getMessage());
        // errorDetail.setProperty("description", "The JWT signature is invalid");
        // }

        if (exception instanceof ExpiredJwtException) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "The JWT token has expired");
            response.put("status", HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        // if (errorDetail == null) {
        // errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),
        // exception.getMessage());
        // errorDetail.setProperty("description", "Unknown internal server error.");
        // }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Unknown internal server error.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
}
