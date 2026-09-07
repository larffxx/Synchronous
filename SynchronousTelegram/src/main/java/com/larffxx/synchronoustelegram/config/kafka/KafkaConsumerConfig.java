package com.larffxx.synchronoustelegram.config.kafka;

import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring configuration that builds Kafka consumer factories for incoming Discord payloads.
 * Creates shared consumer properties and listener container factories for command and message topics.
 */
@Configuration
public class KafkaConsumerConfig {
    /**
     * Kafka bootstrap servers address injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds the shared Kafka consumer properties.
     * @return map of consumer configuration properties
     */
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    /**
     * Creates the consumer factory for message payloads.
     * @return consumer factory for MessagePayload records
     */
    @Bean
    public ConsumerFactory<String, MessagePayload> messagePayloadConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Creates the consumer factory for command payloads.
     * @return consumer factory for CommandPayload records
     */
    @Bean
    public ConsumerFactory<String, CommandPayload> commandPayloadConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Creates the listener container factory for command payloads.
     * @param commandPayloadConsumerFactory consumer factory for command payloads
     * @return listener container factory for CommandPayload records
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommandPayload>> concurrentMessageListenerContainerKafkaListenerContainerFactory(ConsumerFactory<String, CommandPayload> commandPayloadConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CommandPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commandPayloadConsumerFactory);
        return factory;
    }

    /**
     * Creates the listener container factory for message payloads.
     * @param messagePayloadConsumerFactory consumer factory for message payloads
     * @return listener container factory for MessagePayload records
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MessagePayload>> messageListenerContainerKafkaListenerContainerFactory(ConsumerFactory<String, MessagePayload> messagePayloadConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, MessagePayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messagePayloadConsumerFactory);
        return factory;
    }
}
