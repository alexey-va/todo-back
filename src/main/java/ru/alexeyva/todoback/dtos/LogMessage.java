package ru.alexeyva.todoback.dtos;

import lombok.Data;

@Data
public class LogMessage {
    String message;
    long timestamp;
}
