package ru.alexeyva.todoback.exception;

public class NameAlreadyTakenException extends RuntimeException{

    public NameAlreadyTakenException(String message) {
        super(message);
    }

    public NameAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
