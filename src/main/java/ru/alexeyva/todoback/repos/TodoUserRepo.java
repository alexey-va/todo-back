package ru.alexeyva.todoback.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexeyva.todoback.model.TodoUser;

import java.util.List;

public interface TodoUserRepo extends JpaRepository<TodoUser, Long> {

    @Query("SELECT u FROM TodoUser u WHERE u.username = :username")
    @EntityGraph(value = "TodoUser.all")
    TodoUser fetchTodoUserEagerlyAll(String username);

    @Query("SELECT u FROM TodoUser u LEFT JOIN FETCH u.role WHERE u.username = :username")
    TodoUser findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM TodoUser u JOIN FETCH u.stickers sticker JOIN FETCH sticker.tags  WHERE u.username = :username")
    @EntityGraph(attributePaths = {"stickers"})
    TodoUser fetchTodoUserEagerlyWithStickers(String username);

    @Query("SELECT u FROM TodoUser u JOIN FETCH u.tasks WHERE u.username = :username")
    @EntityGraph(attributePaths = {"tasks"})
    TodoUser fetchTodoUserEagerlyWithTasks(String username);

    @Query("SELECT u FROM TodoUser u JOIN FETCH u.tags WHERE u.username = :username")
    @EntityGraph(attributePaths = {"tags"})
    TodoUser fetchTodoUserEagerlyWithTags(String username);

    @Query("SELECT u FROM TodoUser u JOIN FETCH u.taskLists WHERE u.username = :username")
    @EntityGraph(attributePaths = {"taskLists"})
    TodoUser fetchTodoUserEagerlyWithTaskLists(String username);

    @Query("SELECT u FROM TodoUser u")
    @EntityGraph("TodoUser.all")
    List<TodoUser> fetchAll();

}
