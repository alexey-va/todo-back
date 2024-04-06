package ru.alexeyva.todoback.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.exception.notfound.StickerNotFoundException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.Sticker;
import ru.alexeyva.todoback.model.TodoUser;
import ru.alexeyva.todoback.repos.StickerRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;

@Service
@RequiredArgsConstructor
public class StickerService {

    final StickerRepo stickerRepo;
    final TodoUserRepo todoUserRepo;

    @Transactional
    public Sticker createSticker(String username, Sticker stickerDto) {
        TodoUser user = fetchUserWithStickers(username);
        Sticker sticker = user.createSticker(stickerDto);
        user.getStickers().add(sticker);
        return stickerRepo.save(sticker);
    }

    @Transactional
    public Sticker updateSticker(String username, Sticker stickerDto) {
        TodoUser user = fetchUserWithStickers(username);
        Sticker sticker = user.sticker(stickerDto.getLocalId());
        if (sticker == null) throw new StickerNotFoundException(stickerDto.getLocalId());
        if (stickerDto.getTitle() != null) sticker.setTitle(sticker.getTitle());
        if (stickerDto.getColor() != null) sticker.setColor(sticker.getColor());
        if(stickerDto.getText() != null) sticker.setText(stickerDto.getText());
        if(stickerDto.getTags() != null) sticker.setTags(stickerDto.getTags());
        return stickerRepo.save(sticker);
    }

    @Transactional
    public void deleteSticker(String username, Sticker stickerDtp) {
        TodoUser user = fetchUserWithStickers(username);
        Sticker s = user.sticker(stickerDtp.getLocalId());
        if (s == null) throw new StickerNotFoundException(stickerDtp.getLocalId());
        stickerRepo.delete(s);
        user.getStickers().remove(s);
    }

    @NotNull
    private TodoUser fetchUserWithStickers(String username) {
        var user =  todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }

}
