package ru.alexeyva.todoback.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alexeyva.todoback.dtos.TodoResponse;
import ru.alexeyva.todoback.exception.NameAlreadyTakenException;
import ru.alexeyva.todoback.exception.UserAlreadyExistsException;
import ru.alexeyva.todoback.exception.notfound.ElementNotFoundException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;

import java.util.Map;

@RestControllerAdvice
public class TodoExceptionHandler {

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<TodoResponse> handleNoUser(ElementNotFoundException e){
        return TodoResponse.builder()
                .status("error")
                .field("reason", "Element not found. "+e.getFieldName()+" with value "+e.getFieldValue())
                .build()
                .toResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<TodoResponse> handleAlreadyExists(UserAlreadyExistsException e){
        return TodoResponse.builder()
                .status("error")
                .field("reason", "User "+ e.getUsername() +" already exists")
                .build()
                .toResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NameAlreadyTakenException.class)
    public ResponseEntity<TodoResponse> handleNameTaken(NameAlreadyTakenException e){
        return TodoResponse.builder()
                .status("error")
                .field("reason", "Name"+ e.getName()+" already taken")
                .build()
                .toResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TodoResponse> handleUnexpected(Exception e){
        e.printStackTrace();
        return TodoResponse.builder()
                .status("error")
                .field("reason", e.getMessage())
                .build()
                .toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
