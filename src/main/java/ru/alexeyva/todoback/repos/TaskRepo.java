package ru.alexeyva.todoback.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexeyva.todoback.model.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {



}
