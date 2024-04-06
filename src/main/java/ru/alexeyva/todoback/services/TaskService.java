package ru.alexeyva.todoback.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.exception.notfound.TaskNotFoundException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.Task;
import ru.alexeyva.todoback.model.TodoUser;
import ru.alexeyva.todoback.repos.TaskRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;

@Service
@RequiredArgsConstructor
public class TaskService {

    final TaskRepo taskRepo;
    final TodoUserService todoUserService;
    final TodoUserRepo todoUserRepo;


    @Transactional
    @NotNull
    public Task updateTask(String username, Task taskDto) {
        TodoUser user = fetchUserWithTasks(username);

        Task t = user.task(taskDto.getLocalId());
        if (t == null) throw new TaskNotFoundException("Task with id " + taskDto.getLocalId() + " not found");
        if (taskDto.getTitle() != null) t.setTitle(taskDto.getTitle());
        if (taskDto.getCompleted() != null) t.setCompleted(taskDto.getCompleted());
        if (taskDto.getStartDate() != null) t.setStartDate(taskDto.getStartDate());
        if (taskDto.getEndDate() != null) t.setEndDate(taskDto.getEndDate());
        if (taskDto.getTaskListId() != null) t.setTaskListId(taskDto.getTaskListId());
        return taskRepo.save(t);
    }

    @Transactional
    public void deleteTask(String username, Task taskDto) {
        TodoUser user = fetchUserWithTasks(username);
        Task t = user.task(taskDto.getLocalId());
        if (t == null) throw new TaskNotFoundException("Task with id " + taskDto.getLocalId() + " not found");
        taskRepo.delete(t);
        user.getTasks().remove(t);
    }

    @Transactional
    @NotNull
    public Task createTask(String username, Task taskDto) {
        TodoUser user = fetchUserWithTasks(username);
        Task task = user.createTask(taskDto);
        user.getTasks().add(task);
        return taskRepo.save(task);
    }

    @NotNull
    private TodoUser fetchUserWithTasks(String username) {
        var user = todoUserRepo.fetchTodoUserEagerlyWithTasks(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }
}
