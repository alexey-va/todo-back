package ru.alexeyva.todoback.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.dtos.TodoResponse;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.Sticker;
import ru.alexeyva.todoback.repos.TodoUserRepo;
import ru.alexeyva.todoback.services.StickerService;
import ru.alexeyva.todoback.services.TodoUserService;
import ru.alexeyva.todoback.utils.Utils;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user/stickers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StickerController {

    final TodoUserRepo todoUserRepo;
    final TodoUserService todoUserService;
    final StickerService stickerService;

    @GetMapping
    public ResponseEntity<Object> getAllStickers(Principal principal) {
        String username = principal.getName();
        var user = todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException(username);

        return ResponseEntity.ok(user.getStickers());
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createSticker(Principal principal, @RequestBody Sticker stickerDto) {
        String username = principal.getName();
        var sticker = stickerService.createSticker(username, stickerDto);
        if(sticker.getColor() == null) sticker.setColor(Utils.generateSoftColorHex());
        return TodoResponse.builder()
                .status("success")
                .field("sticker", sticker)
                .build()
                .toResponseEntity();
    }

    @PutMapping
    public ResponseEntity<TodoResponse> updateSticker(Principal principal, @RequestBody Sticker stickerDto) {
        String username = principal.getName();
        Sticker sticker = stickerService.updateSticker(username, stickerDto);
        return TodoResponse.builder()
                .status("success")
                .field("sticker", sticker)
                .build()
                .toResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<TodoResponse> deleteSticker(Principal principal, @RequestBody Sticker sticker) {
        String username = principal.getName();
        stickerService.deleteSticker(username, sticker);
        return TodoResponse.builder()
                .status("success")
                .build()
                .toResponseEntity();
    }

}
