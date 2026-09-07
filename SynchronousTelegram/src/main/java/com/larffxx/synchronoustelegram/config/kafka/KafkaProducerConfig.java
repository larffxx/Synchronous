package com.larffxx.synchronoustelegram.config.kafka;

import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring configuration that builds Kafka producer factories for outgoing Telegram payloads.
 * Creates shared producer properties, producer factories, and templates for command and message topics.
 */
@Configuration
public class KafkaProducerConfig {
    /**
     * Kafka bootstrap servers address injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds the shared Kafka producer properties.
     * @return map of producer configuration properties
     */
    public Map<String, Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Creates the producer factory for message payloads.
     * @return producer factory for MessagePayload records
     */
    @Bean
    public ProducerFactory<String, MessagePayload> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }
    /**
     * Creates the producer factory for command payloads.
     * @return producer factory for CommandPayload records
     */
    @Bean
    public ProducerFactory<String, CommandPayload> commandPayloadProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Creates the Kafka template used to send message payloads.
     * @param producerFactory producer factory for message payloads
     * @return Kafka template for MessagePayload records
     */
    @Bean
    public KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate(ProducerFactory<String, MessagePayload> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Creates the Kafka template used to send command payloads.
     * @param commandPayloadProducerFactory producer factory for command payloads
     * @return Kafka template for CommandPayload records
     */
    @Bean
    public KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate(ProducerFactory<String, CommandPayload> commandPayloadProducerFactory){
        return new KafkaTemplate<>(commandPayloadProducerFactory);
    }
}
