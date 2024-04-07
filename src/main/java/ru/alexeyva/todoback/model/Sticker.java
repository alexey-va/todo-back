package ru.alexeyva.todoback.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "stickers",
        indexes = @Index(columnList = "user_id"))
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;

    @Column(columnDefinition = "TEXT")
    String title;
    @Column(columnDefinition = "TEXT")
    String text;
    String color;
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    Set<Tag> tags;

    @ManyToOne
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    TodoUser user;

    @JsonProperty("id")
    @JsonAlias({"localId"})
    Integer localId;

}
