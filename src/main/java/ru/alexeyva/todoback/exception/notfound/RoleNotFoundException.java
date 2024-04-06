package ru.alexeyva.todoback.exception.notfound;

public class RoleNotFoundException extends ElementNotFoundException{
    public RoleNotFoundException(String value) {
        super("Role", value);
    }
}
