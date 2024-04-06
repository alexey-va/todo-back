package ru.alexeyva.todoback.exception.notfound;

public class TaskNotFoundException extends ElementNotFoundException {
    public TaskNotFoundException(String value) {
        super("Task", value);
    }
}
