package ru.alexeyva.todoback.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alexeyva.todoback.exception.NameAlreadyTakenException;
import ru.alexeyva.todoback.exception.UserAlreadyExistsException;
import ru.alexeyva.todoback.exception.UserNotFoundException;

import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNoUser(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExists(Exception e){
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(NameAlreadyTakenException.class)
    public ResponseEntity<Object> handleNameTaken(Exception e){
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Map.of("error", e.getMessage()));
    }

}
