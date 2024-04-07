package ru.alexeyva.todoback.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thedeanda.lorem.LoremIpsum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.exception.UserAlreadyExistsException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.*;
import ru.alexeyva.todoback.repos.TodoUserRepo;
import ru.alexeyva.todoback.utils.Utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TodoUserService {

    final TodoUserRepo todoUserRepo;
    final PasswordEncoder passwordEncoder;
    final RoleService roleService;

    @Transactional
    public TodoUser createUser(TodoUser todoUserDto) {
        if (todoUserRepo.existsByUsername(todoUserDto.getUsername()))
            throw new UserAlreadyExistsException(todoUserDto.getUsername());

        createDefaults(todoUserDto);
        todoUserDto.setRole(roleService.userRole());
        todoUserDto.setPassword(passwordEncoder.encode(todoUserDto.getPassword()));

        return todoUserRepo.save(todoUserDto);
    }

    @Transactional
    public TodoUser updateUser(TodoUser todoUserDto) {
        TodoUser user = todoUserRepo.findByUsername(todoUserDto.getUsername());
        if (user == null) throw new UserNotFoundException(todoUserDto.getUsername());

        if (todoUserDto.getUsername() != null) user.setUsername(todoUserDto.getUsername());
        if (todoUserDto.getPassword() != null) user.setPassword(passwordEncoder.encode(todoUserDto.getPassword()));


        return todoUserRepo.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        TodoUser user = todoUserRepo.findByUsername(username);
        if (user == null) throw new UserNotFoundException(username);

        todoUserRepo.delete(user);
    }

    public boolean existsByUsername(String username) {
        return todoUserRepo.existsByUsername(username);
    }

    @NotNull
    public TodoUser getUserByUsername(String username) {
        TodoUser user = todoUserRepo.findByUsername(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }

    @Transactional
    public void createDefaults(TodoUser todoUser) {
        todoUser.setTaskLists(new HashSet<>());
        todoUser.setTasks(new HashSet<>());
        todoUser.setTags(new HashSet<>());
        todoUser.setStickers(new HashSet<>());

        Tag t1 = Tag.builder().title("Important").color(Utils.generateSoftColorHex()).build();
        Tag t2 = Tag.builder().title("Casual").color(Utils.generateSoftColorHex()).build();
        Tag t3 = Tag.builder().title("Home").color(Utils.generateSoftColorHex()).build();

        todoUser.createTag(t1);
        todoUser.createTag(t2);
        todoUser.createTag(t3);


        TaskList taskList1 = TaskList.builder().title("Inbox").color("#C1FFC1").build();
        TaskList taskList2 = TaskList.builder().title("Work").color("#C1D1FF").build();
        TaskList taskList3 = TaskList.builder().title("Projects").color("#FFC1C1").build();

        todoUser.createTaskList(taskList1);
        todoUser.createTaskList(taskList2);
        todoUser.createTaskList(taskList3);


        Task task1 = Task.builder().title("5 Hour long task")
                .startDate(Instant.now()).endDate(Instant.now().plus(5, ChronoUnit.HOURS))
                .completed(false).build();

        Task task2 = Task.builder().title("3 Hour long task")
                .startDate(Instant.now()).endDate(Instant.now().plus(3, ChronoUnit.HOURS))
                .completed(false).build();

        Task task3 = Task.builder().title("7 Hour long task")
                .startDate(Instant.now()).endDate(Instant.now().plus(7, ChronoUnit.HOURS))
                .completed(false).build();

        task1.setTaskListId(taskList1.getLocalId());
        task2.setTaskListId(taskList2.getLocalId());
        task3.setTaskListId(taskList3.getLocalId());

        todoUser.createTask(task1);
        todoUser.createTask(task2);
        todoUser.createTask(task3);


        String text1 = LoremIpsum.getInstance().getWords(10, 30);
        String text2 = LoremIpsum.getInstance().getWords(10, 30);
        String text3 = LoremIpsum.getInstance().getWords(10, 30);

        Sticker s1 = Sticker.builder().title("Sticker 1").text(text1).color("#FFC1C1").build();
        Sticker s2 = Sticker.builder().title("Sticker 2").text(text2).color("#C1D1FF").build();
        Sticker s3 = Sticker.builder().title("Sticker 3").text(text3).color("#C1FFC1").build();

        s1.setTags(new HashSet<>(List.of(t2, t3)));
        s2.setTags(new HashSet<>(List.of(t1, t3)));
        s3.setTags(new HashSet<>(List.of(t1, t2)));


        todoUser.createSticker(s1);
        todoUser.createSticker(s2);
        todoUser.createSticker(s3);
    }
}
