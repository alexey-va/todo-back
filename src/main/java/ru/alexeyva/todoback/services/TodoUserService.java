package ru.alexeyva.todoback.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.model.Sticker;
import ru.alexeyva.todoback.model.TodoUser;
import ru.alexeyva.todoback.repos.StickerRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;
import ru.alexeyva.todoback.utils.Utils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TodoUserService {

    final TodoUserRepo todoUserRepo;
    final StickerRepo stickerRepo;

    public boolean existsByUsername(String username){
        return todoUserRepo.existsByUsername(username);
    }

    @Transactional
    public Sticker updateSticker(TodoUser todoUser, Sticker update){
        Sticker stickerToUpdate = todoUser.sticker(update.getLocalId());
        if (stickerToUpdate == null){
            return null;
        }

        if (update.getTitle() != null) stickerToUpdate.setTitle(update.getTitle());
        if (update.getText() != null) stickerToUpdate.setText(update.getText());
        if (update.getColor() != null) stickerToUpdate.setColor(update.getColor());
        if (update.getTags() != null) stickerToUpdate.setTags(update.getTags());

        return stickerRepo.save(stickerToUpdate);
    }

    @Transactional
    public Sticker createSticker(TodoUser todoUser, Sticker sticker){
        sticker.setLocalId(todoUser.getStickers().stream().mapToInt(Sticker::getLocalId).max().orElse(-1)+1);
        sticker.setUser(todoUser);
        todoUser.getStickers().add(sticker);
        if(sticker.getColor() == null) sticker.setColor(Utils.generateSoftColorHex());
        return todoUserRepo.save(todoUser).sticker(sticker.getLocalId());
    }

    @Transactional
    public Set<Sticker> deleteSticker(TodoUser todoUser, int localId){
        Sticker sticker = todoUser.sticker(localId);
        if (sticker == null){
            return null;
        }
        todoUser.getStickers().remove(sticker);
        stickerRepo.delete(sticker);
        todoUserRepo.save(todoUser);
        return todoUser.getStickers();
    }



}
