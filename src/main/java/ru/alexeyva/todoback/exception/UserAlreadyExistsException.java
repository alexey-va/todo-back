package ru.alexeyva.todoback.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{

    String username;
    public UserAlreadyExistsException(String username) {
        super();
        this.username = username;
    }
}
