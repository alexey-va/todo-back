package ru.alexeyva.todoback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "todo_users",
        indexes = {@Index(name = "username_index", columnList = "username", unique = true)},
        uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
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

    @ManyToOne
    @JsonIgnore
    Role role;

    @JsonIgnore
    public int getMaxLocalTaskId() {
        if (tasks == null || tasks.isEmpty()) return 0;
        return tasks.stream().mapToInt(Task::getLocalId).max().orElse(0);
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

    @JsonIgnore
    public void createDefaults() {
        Tag t1 = Tag.builder().user(this).title("Important").color("#FFC1C1").localId(0).build();
        Tag t2 = Tag.builder().user(this).title("Casual").color("#C1D1FF").localId(1).build();
        Tag t3 = Tag.builder().user(this).title("Home").color("#C1FFC1").localId(2).build();
        if (tags == null || tags.isEmpty()) {
            tags = new HashSet<>(Set.of(t1, t2, t3));
        }
        if (taskLists == null || taskLists.isEmpty()) {
            Task task1 = Task.builder().title("Task 1").user(this)
                    .startDate(Instant.now()).endDate(Instant.now().plus(5, ChronoUnit.HOURS))
                    .completed(false).localId(0).build();

            Task task2 = Task.builder().title("Task 2").user(this)
                    .startDate(Instant.now()).endDate(Instant.now().plus(3, ChronoUnit.HOURS))
                    .completed(false).localId(1).build();

            Task task3 = Task.builder().title("Task 3").user(this)
                    .startDate(Instant.now()).endDate(Instant.now().plus(7, ChronoUnit.HOURS))
                    .completed(false).localId(2).build();

            TaskList taskList1 = TaskList.builder().user(this).title("Inbox").color("#C1FFC1").localId(0).build();
            TaskList taskList2 = TaskList.builder().user(this).title("Work").color("#C1D1FF").localId(1).build();
            TaskList taskList3 = TaskList.builder().user(this).title("Projects").color("#FFC1C1").localId(2).build();

            task1.setTaskListId(taskList1.localId);
            task2.setTaskListId(taskList2.localId);
            task3.setTaskListId(taskList3.localId);

            taskLists = new HashSet<>(Set.of(taskList1, taskList2, taskList3));
            tasks = new HashSet<>(Set.of(task1, task2, task3));
        }
        if (stickers == null || stickers.isEmpty()) {
            stickers = new HashSet<>(Set.of(
                    Sticker.builder().user(this).title("Sticker 1").text("Sticker 1 text").color("#FFC1C1")
                            .tags(new HashSet<>(List.of(t1))).localId(0).build(),
                    Sticker.builder().user(this).title("Sticker 2").text("Sticker 2 text").color("#C1D1FF")
                            .tags(new HashSet<>(List.of(t2))).localId(1).build(),
                    Sticker.builder().user(this).title("Sticker 3").text("Sticker 3 text").color("#C1FFC1")
                            .tags(new HashSet<>(List.of(t3))).localId(2).build()
            ));
        }


    }

    public Iterable<? extends Task> tasksOfList(TaskList taskList) {
        return tasks.stream().filter(task -> task.taskListId.longValue() == taskList.id)::iterator;
    }
}
