package ru.alexeyva.todoback.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.exception.NameAlreadyTakenException;
import ru.alexeyva.todoback.exception.notfound.TaskListNotFoundException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.TaskList;
import ru.alexeyva.todoback.model.TodoUser;
import ru.alexeyva.todoback.repos.TaskListRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskListService {

    final TodoUserRepo todoUserRepo;
    final TaskListRepo taskListRepo;

    @Transactional
    public TaskList createTaskList(String username, TaskList taskListDto) {
        var user = fetchUserWithTaskLists(username);
        user.getTaskLists().stream()
                .filter(tl -> tl.getTitle().equals(taskListDto.getTitle()))
                .findAny()
                .ifPresent(tl -> {
                    throw new NameAlreadyTakenException("Task list with name " + tl.getTitle() + " already exists", taskListDto.getTitle());
                });
        TaskList taskList = user.createTaskList(taskListDto);
        return taskListRepo.save(taskList);
    }

    @Transactional
    public TaskList updateTaskList(String username, TaskList taskListDto) {
        var user = fetchUserWithTaskLists(username);
        TaskList taskList = user.getTaskLists().stream()
                .filter(tl -> Objects.equals(tl.getLocalId(), taskListDto.getLocalId()))
                .findAny()
                .orElseThrow(() -> new TaskListNotFoundException(taskListDto.getLocalId()));
        taskList.setTitle(taskListDto.getTitle());
        taskList.setColor(taskListDto.getColor());
        return taskListRepo.save(taskList);
    }

    @Transactional
    public void deleteTaskList(String username, TaskList taskListDto) {
        var user = fetchUserWithTaskLists(username);
        Set<TaskList> taskLists = user.getTaskLists();
        TaskList taskList = taskLists.stream()
                .filter(tl -> Objects.equals(tl.getLocalId(), taskListDto.getLocalId()))
                .findAny()
                .orElseThrow(() -> new TaskListNotFoundException(taskListDto.getLocalId()));
        taskLists.remove(taskList);
        taskListRepo.delete(taskList);
    }

    @NotNull
    private TodoUser fetchUserWithTaskLists(String username) {
        var user = todoUserRepo.fetchTodoUserEagerlyWithTaskLists(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }

}
