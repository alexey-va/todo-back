package ru.alexeyva.todoback;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DBInit  implements CommandLineRunner {
    final DataSource dataSource;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from DBInit");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(false, false,
                "UTF-8", new ClassPathResource("data.sql"));
        populator.execute(dataSource);
    }
}
