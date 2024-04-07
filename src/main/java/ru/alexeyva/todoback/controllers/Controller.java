package ru.alexeyva.todoback.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.dtos.TodoResponse;
import ru.alexeyva.todoback.events.RequestEvent;
import ru.alexeyva.todoback.exception.NameAlreadyTakenException;
import ru.alexeyva.todoback.exception.UnauthorizedAccessException;
import ru.alexeyva.todoback.exception.UserAlreadyExistsException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.*;
import ru.alexeyva.todoback.repos.*;
import ru.alexeyva.todoback.services.KafkaService;
import ru.alexeyva.todoback.services.TodoUserService;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class Controller {

    final StickerRepo stickerRepo;
    final TaskListRepo taskListRepo;
    final TodoUserRepo todoUserRepo;
    final TaskRepo taskRepo;
    final TodoUserService todoUserService;
    final RoleRepo roleRepo;
    final PasswordEncoder passwordEncoder;
    final ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("login")
    public ResponseEntity<Object> login(Principal principal) {
        log.info("User logged in: {}", principal.getName());
        return ResponseEntity.ok(Map.of("status", "success", "username", principal.getName()));
    }

    @PostMapping("register")
    public ResponseEntity<TodoResponse> register(@RequestBody TodoUser todoUser) {
        var user = todoUserService.createUser(todoUser);
        log.info("User registered: {}", user.getUsername());
        return TodoResponse.builder()
                .status("success")
                .field("user", user)
                .build()
                .toResponseEntity();
    }


    @GetMapping("allusers")
    public ResponseEntity<TodoResponse> getAllUsers() {
        return TodoResponse.builder()
                .status("success")
                .field("users", todoUserRepo.findAll())
                .build()
                .toResponseEntity();
    }


    @GetMapping("user")
    public ResponseEntity<TodoUser> getAllDataOfUser(Principal principal, HttpServletRequest request) {
        String username = principal.getName();
        String realIp = request.getHeader("X-Forwarded-For");

        log.info("User {} requested all data: {}", realIp, username);
        applicationEventPublisher.publishEvent(new RequestEvent(realIp, username));

        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("user")
    public ResponseEntity<TodoResponse> updateUser(Principal principal, @RequestBody TodoUser todoUser) {
        String username = principal.getName();
        if (!Objects.equals(username, todoUser.getUsername()))
            throw new UnauthorizedAccessException("Non admin user tried to change someone's username", "admin");

        var user = todoUserService.updateUser(todoUser);
        return TodoResponse.builder()
                .status("success")
                .field("user", user)
                .build()
                .toResponseEntity();
    }

    // trash
    @DeleteMapping("user")
    public ResponseEntity<TodoResponse> deleteUser(Principal principal) {
        String username = principal.getName();
        todoUserService.deleteUser(username);
        return TodoResponse.builder()
                .status("success")
                .build()
                .toResponseEntity();
    }
}
