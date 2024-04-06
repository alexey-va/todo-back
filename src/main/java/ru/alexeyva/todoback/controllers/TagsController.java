package ru.alexeyva.todoback.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexeyva.todoback.dtos.TodoResponse;
import ru.alexeyva.todoback.model.Tag;
import ru.alexeyva.todoback.services.TagsService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TagsController {

    final TagsService tagsService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTag(Principal principal, @RequestBody Tag tagDto) {
        String username = principal.getName();
        var tag = tagsService.createTag(username, tagDto);
        return TodoResponse.builder()
                .status("success")
                .field("tag", tag)
                .build()
                .toResponseEntity();
    }

    @PutMapping
    public ResponseEntity<TodoResponse> updateTag(Principal principal, @RequestBody Tag update) {
        String username = principal.getName();
        var tag = tagsService.updateTag(username, update);
        return TodoResponse.builder()
                .status("success")
                .field("tag", tag)
                .build()
                .toResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<TodoResponse> deleteTag(Principal principal, @RequestBody Tag tagDto) {
        String username = principal.getName();
        tagsService.deleteTag(username, tagDto);
        return TodoResponse.builder()
                .status("success")
                .build()
                .toResponseEntity();
    }

}
