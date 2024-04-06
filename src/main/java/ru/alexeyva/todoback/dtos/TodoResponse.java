package ru.alexeyva.todoback.dtos;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = TodoResponse.TodoResponseSerializer.class)
public class TodoResponse {

    Map<String, Object> fields;

    public static TodoResponseBuilder builder(){
        return new TodoResponseBuilder();
    }

    public ResponseEntity<TodoResponse> toResponseEntity(int code){
        return ResponseEntity.status(code).body(this);
    }

    public ResponseEntity<TodoResponse> toResponseEntity(){
        return toResponseEntity(HttpStatus.OK.value());
    }

    public ResponseEntity<TodoResponse> toResponseEntity(HttpStatus status){
        return toResponseEntity(status.value());
    }

    static class TodoResponseSerializer extends JsonSerializer<TodoResponse> {

        @Override
        public void serialize(TodoResponse todoResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            todoResponse.fields.forEach((s, o) -> {
                try {
                    jsonGenerator.writeObjectField(s, o);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonGenerator.writeEndObject();
        }
    }


    public static class TodoResponseBuilder {

        String status;
        Map<String, Object> fields = new HashMap<>();

        public TodoResponseBuilder status(String status){
            this.status = status;
            return this;
        }

        public TodoResponseBuilder field(String s, Object o){
            fields.put(s, o);
            return this;
        }

        public TodoResponse build(){
            fields.put("status", status);
            return new TodoResponse(fields);
        }

    }
}
