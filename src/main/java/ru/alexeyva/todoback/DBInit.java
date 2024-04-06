package ru.alexeyva.todoback;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
@Slf4j
public class DBInit  implements CommandLineRunner {
    final DataSource dataSource;
    @Override
    public void run(String... args) throws Exception {
        log.info("Populating roles table");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(false, false,
                "UTF-8", new ClassPathResource("data.sql"));
        populator.execute(dataSource);
    }
}
