package ru.alexeyva.todoback.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks",
        indexes = @Index(columnList = "user_id"))
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore

    Long id;

    String title;
    Boolean completed;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    Instant startDate;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    Instant endDate;


    @JsonProperty("list")
    Integer taskListId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    TodoUser user;

    @JsonProperty("id")
    @JsonAlias({"localId"})
    Integer localId;

}

class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getEpochSecond());
    }
}

class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        return Instant.ofEpochSecond(p.getLongValue());
    }
}
