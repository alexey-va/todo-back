package ru.alexeyva.todoback.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedeanda.lorem.LoremIpsum;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseTest {

    @Test
    @SneakyThrows
    void testFieldsSerializing() {
        TodoResponse response = TodoResponse.builder()
                .status("success")
                .field("field1", "value1")
                .field("field2", Map.of("key1", "value1", "key2", "value2"))
                .build();
        System.out.println(response);
        String json = new ObjectMapper().writeValueAsString(response);
        assertTrue(json.contains("field1"));
        assertTrue(json.contains("field2"));
        assertTrue(json.contains("value1"));
        assertTrue(json.contains("value2"));
        assertTrue(json.contains("status"));
        assertTrue(json.contains("success"));
        System.out.println(json);
    }

    @Test
    public void testLorem(){
        System.out.println(LoremIpsum.getInstance().getParagraphs(1,3));
    }

}