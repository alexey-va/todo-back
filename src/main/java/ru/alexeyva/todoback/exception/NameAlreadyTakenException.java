package ru.alexeyva.todoback.exception;

import lombok.Getter;

@Getter
public class NameAlreadyTakenException extends RuntimeException{

    String name;
    public NameAlreadyTakenException(String message, String name) {
        super(message);
        this.name = name;
    }

}
