package ru.alexeyva.todoback.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.dtos.LogMessage;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
@Profile("kafka")
@RequiredArgsConstructor
public class KafkaService {

    private Deque<LogMessage> logs = new ConcurrentLinkedDeque<>();
    private Set<String> requests = new HashSet<>();
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "todo_logs", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        LogMessage logMessage = new LogMessage();
        logMessage.setMessage(message);
        logMessage.setTimestamp(System.currentTimeMillis());
        logs.add(logMessage);
    }

    @KafkaListener(topics = "todo_requests", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeRequest(String message) {
        requests.add(message);
    }

    public List<LogMessage> getLogs(long from, long to) {
        return logs.stream()
                .filter(log -> log.getTimestamp() >= from && log.getTimestamp() <= to)
                .toList();
    }

/*    public void publishRequest(String remoteAddr, String userRequested){
        //System.out.println("Publishing request");
        //System.out.println(requests);
        if(requests.contains(remoteAddr + ";;;" + userRequested)) return;
        kafkaTemplate.send("todo_requests", remoteAddr + ";;;" + userRequested);
    }*/

}

