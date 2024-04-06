package ru.alexeyva.todoback.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.exception.notfound.TagNotFoundException;
import ru.alexeyva.todoback.exception.notfound.UserNotFoundException;
import ru.alexeyva.todoback.model.Tag;
import ru.alexeyva.todoback.model.TodoUser;
import ru.alexeyva.todoback.repos.TagRepo;
import ru.alexeyva.todoback.repos.TodoUserRepo;

@Service
@RequiredArgsConstructor
public class TagsService {

    final TodoUserRepo todoUserRepo;
    final TagRepo tagRepo;


    @Transactional
    public Tag createTag(String username, Tag tagDto) {
        TodoUser user = fetchUserWithTags(username);
        Tag tag = user.createTag(tagDto);
        user.getTags().add(tag);
        return tagRepo.save(tag);
    }

    @Transactional
    public Tag updateTag(String username, Tag tagDto) {
        TodoUser user = fetchUserWithTags(username);
        Tag tag = user.tag(tagDto.getLocalId());
        if (tag == null) throw new TagNotFoundException(tagDto.getLocalId());
        if (tagDto.getTitle() != null) tag.setTitle(tagDto.getTitle());
        if (tagDto.getColor() != null) tag.setColor(tagDto.getColor());
        return tagRepo.save(tag);
    }

    @Transactional
    public void deleteTag(String username, Tag tagDto) {
        TodoUser user = fetchUserWithTags(username);
        Tag t = user.tag(tagDto.getLocalId());
        if (t == null) throw new TagNotFoundException(tagDto.getLocalId());
        tagRepo.delete(t);
        user.getTags().remove(t);
    }


    @NotNull
    private TodoUser fetchUserWithTags(String username) {
        var user =  todoUserRepo.fetchTodoUserEagerlyWithStickers(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }

}
