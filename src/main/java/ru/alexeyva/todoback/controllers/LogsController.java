package ru.alexeyva.todoback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.dtos.LogMessage;
import ru.alexeyva.todoback.services.KafkaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@CrossOrigin(origins = "*")
public class LogsController {

    @Autowired(required = false)
    private KafkaService kafkaService;

    @GetMapping
    public ResponseEntity<List<String>> getLogs(@RequestParam(required = false, name = "from") Long fromPar,
                                                    @RequestParam(required = false, name = "to") Long toPar,
                                                @RequestParam(required = false, name = "limit") Integer limitPar) {
        long from = fromPar == null ?
        System.currentTimeMillis() - 1000 * 60 * 5
        : fromPar;
        long to = toPar == null ? System.currentTimeMillis() : toPar;
        int limit = limitPar == null ? 300 : limitPar;
        if (limit > 500) limit = 500;

        if (kafkaService == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(kafkaService.getLogs(from, to).stream().limit(limit).map(LogMessage::getMessage).toList());
    }
}
