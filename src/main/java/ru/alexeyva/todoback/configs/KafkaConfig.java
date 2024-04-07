package ru.alexeyva.todoback.configs;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("kafka")
public class KafkaConfig {


    @Bean
    public ConsumerFactory<String, String> consumerFactory(Environment env) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("KAFKA_BOOTSTRAP"));
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(Environment env) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(env));
        return factory;
    }

/*
    @Bean
    public ProducerFactory<String, String> producerFactory(Environment env) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("KAFKA_BOOTSTRAP"));
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(Environment env) {
        return new KafkaTemplate<>(producerFactory(env));
    }

    @Bean
    KafkaAdmin kafkaAdmin(Environment env) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("KAFKA_BOOTSTRAP"));
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic logsTopic() {
        NewTopic logsTopic = new NewTopic("todo_logs", 1, (short) 1);
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", String.valueOf(1000 * 60 * 60 * 24));
        logsTopic.configs(configs);
        return logsTopic;
    }

    @Bean
    NewTopic requestsTopic() {
        return new NewTopic("todo_requests", 1, (short) 1);
    }
*/

}
