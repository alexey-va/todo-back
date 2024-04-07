package ru.alexeyva.todoback.configs;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
@Slf4j
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

    @Bean
    public RedisTemplate<String, String> redisTemplate(Environment environment){
        try {
            String redisHost = environment.getProperty("REDIS_HOST");
            String redisPortString = environment.getProperty("REDIS_PORT");
            int redisPort = redisPortString == null ? 6379 : Integer.parseInt(redisPortString);
            String redisPassword = environment.getProperty("REDIS_PASSWORD");

            if(redisHost == null) throw new Exception("Redis host not configured");


            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
            if(redisPassword != null) redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));

            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);

            RedisTemplate<String, String> rt = new RedisTemplate<>();
            rt.setConnectionFactory(lettuceConnectionFactory);
            rt.afterPropertiesSet();

            return rt;
        } catch (Exception e) {
            log.error("Redis not configured");
            return null;
        }
    }

}
