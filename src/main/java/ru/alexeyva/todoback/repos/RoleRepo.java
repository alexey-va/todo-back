package ru.alexeyva.todoback.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeyva.todoback.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
}
