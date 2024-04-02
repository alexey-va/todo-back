package ru.alexeyva.todoback.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.exception.NameAlreadyTakenException;
import ru.alexeyva.todoback.exception.UserAlreadyExistsException;
import ru.alexeyva.todoback.exception.UserNotFoundException;
import ru.alexeyva.todoback.model.*;
import ru.alexeyva.todoback.repos.RoleRepo;
import ru.alexeyva.todoback.repos.StickerRepo;
import ru.alexeyva.todoback.repos.TaskListRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;
import ru.alexeyva.todoback.services.TodoUserService;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {

    final StickerRepo stickerRepo;
    final TaskListRepo taskListRepo;
    final TodoUserRepo todoUserRepo;
    final TodoUserService todoUserService;
    final RoleRepo roleRepo;
    final PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity<Object> login(Principal principal) {

        return ResponseEntity.ok(Map.of("status", "success", "username", principal.getName()));
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody TodoUser todoUser) {
        if (todoUserRepo.existsByUsername(todoUser.getUsername()))
            throw new UserAlreadyExistsException("Username " + todoUser.getUsername() + " is already taken!");

        todoUser.createDefaults();
        Role userRole = roleRepo.findById(1L).orElse(null);
        todoUser.setRole(userRole);
        todoUser.setPassword(passwordEncoder.encode(todoUser.getPassword()));

        return ResponseEntity.ok(todoUserRepo.save(todoUser));
    }


    @GetMapping("allusers")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(todoUserRepo.fetchAll());
    }

    @PostMapping("users")
    public ResponseEntity<Object> createUser(@RequestBody TodoUser todoUser) {
        //System.out.println(todoUser);

        if (todoUserRepo.existsByUsername(todoUser.getUsername()))
            throw new UserAlreadyExistsException("Username " + todoUser.getUsername() + " is already taken!");

        todoUser.createDefaults();

        return ResponseEntity.ok(todoUserRepo.save(todoUser));
    }

    @GetMapping("user")
    public ResponseEntity<TodoUser> getAllDataOfUser(Principal principal) {
        String username = principal.getName();
        //System.out.println("Get all data of user " + username);
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        return ResponseEntity.ok(user);
    }

    // trash
    @PutMapping("user")
    public ResponseEntity<Object> updateUser(Principal principal, @RequestBody TodoUser todoUser) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        if (todoUser.getUsername() != null) user.setUsername(todoUser.getUsername());
        if (todoUser.getPassword() != null) user.setPassword(todoUser.getPassword());

        todoUserRepo.save(user);
        return ResponseEntity.ok(user);
    }

    // trash
    @DeleteMapping("user")
    public ResponseEntity<Object> deleteUser(Principal principal) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        todoUserRepo.delete(user);
        return ResponseEntity.ok(Map.of("status", "ok", "username", username));
    }


    @GetMapping("user/tasklists")
    public ResponseEntity<Object> getAllTaskLists(Principal principal) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        return ResponseEntity.ok(user.getTaskLists());
    }

    @PostMapping("user/tasklists")
    public ResponseEntity<Object> createTaskList(Principal principal, @RequestBody TaskList taskList) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);


        Set<TaskList> taskLists = user.getTaskLists();
        int maxLocalId = -1;
        for (TaskList tl : taskLists) {
            if (tl.getTitle().equals(taskList.getTitle()))
                throw new NameAlreadyTakenException("Task list with name " + taskList.getTitle() + " already exists!");
            if (tl.getLocalId() > maxLocalId) maxLocalId = tl.getLocalId();
        }

        taskList.setLocalId(maxLocalId + 1);
        taskList.setUser(user);
        taskLists.add(taskList);

        todoUserRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "taskList", taskList));
    }

    @PutMapping("user/tasklists")
    public ResponseEntity<Object> updateTaskList(Principal principal, @RequestBody TaskList update) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        TaskList taskList = user.taskList(update.getLocalId());
        if (taskList == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Task list with id " + update.getLocalId() + " not found"));

        if (taskList.getTitle() != null) taskList.setTitle(taskList.getTitle());
        if (taskList.getColor() != null) taskList.setColor(taskList.getColor());
        return ResponseEntity.ok(Map.of("status", "success", "taskList", todoUserRepo.save(user).getTaskLists()));
    }

    @DeleteMapping("user/tasklists")
    public ResponseEntity<Object> deleteTaskList(Principal principal, @RequestParam("local_id") int localId) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        TaskList taskList = user.taskList(localId);
        if (taskList == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Task list with id " + localId + " not found"));

        user.getTaskLists().remove(taskList);
        todoUserRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "taskLists", user.getTaskLists()));
    }

    @PostMapping("user/tasks")
    public ResponseEntity<Object> createTask(Principal principal, @RequestBody Task task, @RequestParam("task_list") int taskListId) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        TaskList taskList = user.taskList(taskListId);
        if (taskList == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Task list with id " + task.getTaskListId() + " not found"));


        int maxLocalId = user.getMaxLocalTaskId();

        task.setLocalId(maxLocalId + 1);
        task.setUser(user);
        task.setTaskListId(taskList.getLocalId());
        user.getTasks().add(task);
        //System.out.println(user.getTasks());
        todoUserRepo.save(user);

        return ResponseEntity.ok(Map.of("status", "success", "task", task));
    }

    @PutMapping("user/tasks")
    public ResponseEntity<Object> updateTask(Principal principal, @RequestBody Task task) {
        String username = principal.getName();

        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        //System.out.println(task.getLocalId());
        Task t = user.task(task.getLocalId());
        if (t == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Task with id " + task.getLocalId() + " not found"));
        //System.out.println(task);
        if (task.getTitle() != null) t.setTitle(task.getTitle());
        if (task.getCompleted() != null) t.setCompleted(task.getCompleted());
        if (task.getStartDate() != null) t.setStartDate(task.getStartDate());
        if (task.getEndDate() != null) t.setEndDate(task.getEndDate());
        if (task.getTaskListId() != null) t.setTaskListId(task.getTaskListId());

        todoUserRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "task", t));
    }

    @DeleteMapping("user/tasks")
    public ResponseEntity<Object> deleteTask(Principal principal, @RequestBody Task task) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        if (task == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Task not found"));

        boolean deleted = user.getTasks().removeIf(t -> Objects.equals(t.getLocalId(), task.getLocalId()));
        if(!deleted){
            return ResponseEntity.status(404)
                    .body(Map.of("status", "error", "message", "Task with id " + task.getLocalId() + " not found"));
        }
        todoUserRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "tasks", user.getTasks()));
    }

    @GetMapping("user/tags")
    public ResponseEntity<Object> getAllTags(Principal principal) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        return ResponseEntity.ok(user.getTags());
    }

    @PostMapping("user/tags")
    public ResponseEntity<Object> createTag(Principal principal, @RequestBody Tag tag) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        Set<Tag> tags = user.getTags();
        for (Tag t : tags) {
            if (t.getTitle().equals(tag.getTitle()))
                throw new NameAlreadyTakenException("Tag with name " + tag.getTitle() + " already exists!");
        }

        tag.setUser(user);
        tags.add(tag);

        todoUserRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "tag", tag));
    }

    @PutMapping("user/tags")
    public ResponseEntity<Object> updateTag(Principal principal, @RequestBody Tag update) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        Tag tag = user.tag(update.getLocalId());
        if (tag == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Tag with id " + update.getLocalId() + " not found"));

        if (tag.getTitle() != null) tag.setTitle(tag.getTitle());
        if (tag.getColor() != null) tag.setColor(tag.getColor());
        return ResponseEntity.ok(Map.of("status", "success", "tag", todoUserRepo.save(user).getTags()));
    }

    @DeleteMapping("user/tags")
    public ResponseEntity<Object> deleteTag(Principal principal, @RequestParam("local_id") int localId) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyAll(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        Tag tag = user.tag(localId);
        if (tag == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Tag with id " + localId + " not found"));

        user.getTags().remove(tag);
        todoUserRepo.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "tags", user.getTags()));
    }

    @GetMapping("user/stickers")
    public ResponseEntity<Object> getAllStickers(Principal principal) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        return ResponseEntity.ok(user.getStickers());
    }

    @PostMapping("user/stickers")
    public ResponseEntity<Object> createSticker(Principal principal, @RequestBody Sticker sticker) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        Sticker result = todoUserService.createSticker(user, sticker);
        return ResponseEntity.ok(Map.of("status", "success", "sticker", result));
    }

    @PutMapping("user/stickers")
    public ResponseEntity<Object> updateSticker(Principal principal, @RequestBody Sticker sticker) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        Sticker result = todoUserService.updateSticker(user, sticker);
        if (sticker == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Sticker with id " + sticker.getLocalId() + " not found"));
        return ResponseEntity.ok(Map.of("status", "success", "sticker", result));
    }

    @DeleteMapping("user/stickers")
    public ResponseEntity<Object> deleteSticker(Principal principal, @RequestParam("local_id") int localId) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException("No user with name " + username);

        var stickers = todoUserService.deleteSticker(user, localId);
        if (stickers == null) return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", "Sticker with id " + localId + " not found"));

        return ResponseEntity.ok(Map.of("status", "success", "stickers", stickers));
    }


}
