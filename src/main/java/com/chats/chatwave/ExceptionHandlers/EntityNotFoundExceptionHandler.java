package com.chats.chatwave.ExceptionHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.model.ErrorMessage;

@ControllerAdvice
@ResponseStatus
public class EntityNotFoundExceptionHandler {

    @SuppressWarnings("null")
    @ExceptionHandler(EntityNotFoundException.class)

    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(exception.getMessage());
        message.setStatus(exception.getStatus());

        return new ResponseEntity<ErrorMessage>(message, exception.getStatus());
    }
}
