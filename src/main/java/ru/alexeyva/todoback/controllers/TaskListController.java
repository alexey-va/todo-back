package ru.alexeyva.todoback.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.dtos.TodoResponse;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.TaskList;
import ru.alexeyva.todoback.repos.TodoUserRepo;
import ru.alexeyva.todoback.services.TaskListService;
import ru.alexeyva.todoback.services.TodoUserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user/tasklists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskListController {

    final TodoUserRepo todoUserRepo;
    final TodoUserService todoUserService;
    final TaskListService taskListService;

    @GetMapping
    public ResponseEntity<Object> getAllTaskLists(Principal principal) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyWithTaskLists(username);
        if (user == null) throw new UserNotFoundException(username);

        return ResponseEntity.ok(user.getTaskLists());
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTaskList(Principal principal, @RequestBody TaskList taskListDto) {
        String username = principal.getName();
        TaskList taskList = taskListService.createTaskList(username, taskListDto);
        return TodoResponse.builder()
                .status("success")
                .field("taskList", taskList)
                .build()
                .toResponseEntity();
    }

    @PutMapping
    public ResponseEntity<TodoResponse> updateTaskList(Principal principal, @RequestBody TaskList update) {
        String username = principal.getName();
        var taskList = taskListService.updateTaskList(username, update);
        return TodoResponse.builder()
                .status("success")
                .field("taskList", taskList)
                .build()
                .toResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<TodoResponse> deleteTaskList(Principal principal, @RequestBody TaskList taskListDto) {
        String username = principal.getName();
        taskListService.deleteTaskList(username, taskListDto);
        return TodoResponse.builder()
                .status("success")
                .build()
                .toResponseEntity();
    }


}
