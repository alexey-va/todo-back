package ru.alexeyva.todoback.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_lists",
        indexes = @Index(columnList = "user_id"))
public class TaskList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;

    String title;
    String color;


    @ManyToOne
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    TodoUser user;

    @JsonProperty("id")
    @JsonAlias({"localId"})
    Integer localId;

}
