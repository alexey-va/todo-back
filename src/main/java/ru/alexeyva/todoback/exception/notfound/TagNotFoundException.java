package ru.alexeyva.todoback.exception.notfound;

public class TagNotFoundException extends ElementNotFoundException{
    public TagNotFoundException(int value) {
        super("Tag", value+"");
    }
}
