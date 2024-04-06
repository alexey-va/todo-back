package ru.alexeyva.todoback.exception.notfound;

public class TaskListNotFoundException extends ElementNotFoundException {
    public TaskListNotFoundException(int localId) {
        super("Task list", localId+"");
    }
}
