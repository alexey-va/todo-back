package ru.alexeyva.todoback.exception.notfound;

import lombok.Getter;

@Getter
public abstract class ElementNotFoundException extends RuntimeException {

    String fieldName;
    String fieldValue;
    public ElementNotFoundException(String fieldName, String fieldValue) {
        super();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
