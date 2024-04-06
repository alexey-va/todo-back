package ru.alexeyva.todoback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "todo_users",
        indexes = {@Index(name = "username_index", columnList = "username", unique = true)},
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })

@NamedEntityGraph(name = "TodoUser.all",
        attributeNodes = {
                @NamedAttributeNode("taskLists"),
                @NamedAttributeNode("tags"),
                @NamedAttributeNode(value = "stickers", subgraph = "stickers.tags"),
                @NamedAttributeNode("tasks"),
                @NamedAttributeNode("role")
        },
        subgraphs = {
                @NamedSubgraph(name = "stickers.tags", attributeNodes = @NamedAttributeNode("tags"))
        })
public class TodoUser {

    @Id
    @GeneratedValue
    Long id;

    String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<TaskList> taskLists;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Tag> tags;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Sticker> stickers;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Task> tasks;

    @JsonIgnore
    int maxLocalTaskId = 0;
    @JsonIgnore
    int maxLocalTaskListId = 0;
    @JsonIgnore
    int maxLocalTagId = 0;
    @JsonIgnore
    int maxLocalStickerId = 0;

    @ManyToOne
    @JsonIgnore
    Role role;


    public Task createTask(Task task) {
        if (tasks == null) tasks = new HashSet<>();
        task.setLocalId(++maxLocalTaskId);
        task.setUser(this);
        tasks.add(task);
        return task;
    }

    public TaskList createTaskList(TaskList taskList) {
        if (taskLists == null) taskLists = new HashSet<>();
        taskList.setLocalId(++maxLocalTaskListId);
        taskList.setUser(this);
        taskLists.add(taskList);
        return taskList;
    }

    public Tag createTag(Tag tag) {
        if (tags == null) tags = new HashSet<>();
        tag.setLocalId(++maxLocalTagId);
        tag.setUser(this);
        tags.add(tag);
        return tag;
    }

    public Sticker createSticker(Sticker sticker) {
        if (stickers == null) stickers = new HashSet<>();
        sticker.setLocalId(++maxLocalStickerId);
        sticker.setUser(this);
        stickers.add(sticker);
        return sticker;
    }

    @JsonIgnore
    public Task task(String title) {
        if (tasks == null || tasks.isEmpty()) return null;
        return tasks.stream().filter(task -> task.title.equals(title)).findAny().orElse(null);
    }

    @JsonIgnore
    public Task task(int localId) {
        if (tasks == null || tasks.isEmpty()) return null;
        return tasks.stream().filter(task -> task.localId == localId).findAny().orElse(null);
    }

    @JsonIgnore
    public TaskList taskList(String title) {
        if (taskLists == null || taskLists.isEmpty()) return null;
        return taskLists.stream().filter(taskList -> taskList.title.equals(title)).findAny().orElse(null);
    }

    @JsonIgnore
    public TaskList taskList(int localId) {
        if (taskLists == null || taskLists.isEmpty()) return null;
        return taskLists.stream().filter(taskList -> taskList.localId == localId).findAny().orElse(null);
    }

    @JsonIgnore
    public Tag tag(String title) {
        if (tags == null || tags.isEmpty()) return null;
        return tags.stream().filter(tag -> tag.title.equals(title)).findAny().orElse(null);
    }

    @JsonIgnore
    public Tag tag(int localId) {
        if (tags == null || tags.isEmpty()) return null;
        return tags.stream().filter(tag -> tag.localId == localId).findAny().orElse(null);
    }

    @JsonIgnore
    public Sticker sticker(String title) {
        if (stickers == null || stickers.isEmpty()) return null;
        return stickers.stream().filter(sticker -> sticker.title.equals(title)).findAny().orElse(null);
    }

    @JsonIgnore
    public Sticker sticker(int localId) {
        if (stickers == null || stickers.isEmpty()) return null;
        return stickers.stream().filter(sticker -> sticker.localId == localId).findAny().orElse(null);
    }



    public Iterable<? extends Task> tasksOfList(TaskList taskList) {
        return tasks.stream().filter(task -> task.taskListId.longValue() == taskList.id)::iterator;
    }
}
