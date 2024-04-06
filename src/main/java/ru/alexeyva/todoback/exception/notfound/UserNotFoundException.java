package ru.alexeyva.todoback.exception.notfound;

public class UserNotFoundException extends ElementNotFoundException{

    public UserNotFoundException(String username) {
        super("User", username);
    }
}
