package ru.alexeyva.todoback.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeyva.todoback.model.Sticker;

public interface StickerRepo extends JpaRepository<Sticker, Long> {
}
