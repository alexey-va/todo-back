package ru.alexeyva.todoback.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeyva.todoback.model.TaskList;

public interface TaskListRepo extends JpaRepository<TaskList, Long> {
}
