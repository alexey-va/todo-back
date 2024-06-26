package ru.alexeyva.todoback.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexeyva.todoback.model.Tag;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long>{
}
