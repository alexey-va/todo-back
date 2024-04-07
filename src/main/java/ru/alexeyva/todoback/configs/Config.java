package ru.alexeyva.todoback.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Bean
    DataSource dataSource(Environment environment) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("org.postgresql.Driver");

        String dbUrl = environment.getProperty("DB_URL");
        if(dbUrl == null) dbUrl = "jdbc:postgresql://localhost:5432/todo";
        hikariDataSource.setJdbcUrl(dbUrl);
        hikariDataSource.setUsername(environment.getProperty("DB_USER"));
        hikariDataSource.setPassword(environment.getProperty("DB_PASS"));

        return hikariDataSource;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
