package ru.alexeyva.todoback;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoBackApplication.class, args);
        //log.info("Application started");
    }

}
