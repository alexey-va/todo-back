package ru.alexeyva.todoback.services;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.dtos.LogMessage;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("kafka")
public class KafkaService {

    private final List<LogMessage> logs = new ArrayList<>();

    @KafkaListener(topics = "todo_logs", groupId = "group_id")
    public void consume(String message) {
        LogMessage logMessage = new LogMessage();
        logMessage.setMessage(message);
        logMessage.setTimestamp(System.currentTimeMillis());
        logs.add(logMessage);
    }

    public List<LogMessage> getLogs(long from, long to) {
        return logs.stream()
                .filter(log -> log.getTimestamp() >= from && log.getTimestamp() <= to)
                .toList();
    }

}

