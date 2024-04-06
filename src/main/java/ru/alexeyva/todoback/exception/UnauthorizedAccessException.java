package ru.alexeyva.todoback.exception;

public class UnauthorizedAccessException extends RuntimeException{
    String roleRequired;
    public UnauthorizedAccessException(String message, String roleRequired) {
        super(message);
        this.roleRequired = roleRequired;
    }
}
