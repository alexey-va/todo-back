package ru.alexeyva.todoback.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.dtos.TodoResponse;
import ru.alexeyva.todoback.model.Task;
import ru.alexeyva.todoback.repos.TaskRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;
import ru.alexeyva.todoback.services.TaskService;
import ru.alexeyva.todoback.services.TodoUserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {

    final TodoUserRepo todoUserRepo;
    final TaskRepo taskRepo;
    final TodoUserService todoUserService;
    final TaskService taskService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTask(Principal principal, @RequestBody Task taskDto) {
        Task task = taskService.createTask(principal.getName(), taskDto);

        return TodoResponse.builder()
                .status("success")
                .field("task", task)
                .build()
                .toResponseEntity();
    }

    @PutMapping
    public ResponseEntity<TodoResponse> updateTask(Principal principal, @RequestBody Task taskDto) {
        Task t = taskService.updateTask(principal.getName(), taskDto);

        return TodoResponse.builder()
                .status("success")
                .field("task", t)
                .build()
                .toResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<TodoResponse> deleteTask(Principal principal, @RequestBody Task task) {
        taskService.deleteTask(principal.getName(), task);

        return TodoResponse.builder()
                .status("success")
                .build()
                .toResponseEntity();
    }
}
