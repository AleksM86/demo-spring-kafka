package com.example.DemoMockKafka.configuration;

import com.example.DemoMockKafka.model.KafkaMessage;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.kafkaMessageGroupId}")
    private String kafkaMessageGroupId;

    @Bean
    public ProducerFactory<String, KafkaMessage> kafkaMessageProducerFactory (ObjectMapper objectMapper){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(),
                new org.springframework.kafka.support.serializer.JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate(ProducerFactory<String, KafkaMessage> kafkaMessageProducerFactory){
        return new KafkaTemplate<>(kafkaMessageProducerFactory);
    }

    @Bean
    public ConsumerFactory<String, KafkaMessage> kafkaMessageConsumerFactory(ObjectMapper objectMapper){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> concurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, KafkaMessage> kafkaMessageConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory <String, KafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaMessageConsumerFactory);

        return factory;
    }
}
