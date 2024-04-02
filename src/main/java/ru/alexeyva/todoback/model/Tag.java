package ru.alexeyva.todoback.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "tags",
        indexes = @Index(columnList = "user_id"))
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;

    String title;
    String color;


    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")

    @ManyToOne
            @ToString.Exclude
    TodoUser user;

    @JsonProperty("id")
    @JsonAlias({"localId"})
    Integer localId;
}
