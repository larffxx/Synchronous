package com.larffxx.synchronousdiscord.config.kafka;

import com.larffxx.synchronousdiscord.infrastructure.payload.CommandPayload;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
 * Kafka Consumer Config class.
 */
@Configuration
public class KafkaConsumerConfig {

    /**
     * The bootstrap servers.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Consumer Config.
     * @return the resulting object.
     */
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    /**
     * Creates message payload consumer factory bean.
     * @return the resulting message payload.
     */
    @Bean
    public ConsumerFactory<String, MessagePayload> messagePayloadConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Creates command payload consumer factory bean.
     * @return the resulting command payload.
     */
    @Bean
    public ConsumerFactory<String, CommandPayload> commandPayloadConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Creates concurrent message listener container kafka listener container factory bean.
     * @param commandPayloadConsumerFactory the command payload consumer factory.
     * @return the resulting command payload.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommandPayload>> concurrentMessageListenerContainerKafkaListenerContainerFactory(ConsumerFactory<String, CommandPayload> commandPayloadConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CommandPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commandPayloadConsumerFactory);
        return factory;
    }

    /**
     * Creates message listener container kafka listener container factory bean.
     * @param messagePayloadConsumerFactory the message payload consumer factory.
     * @return the resulting message payload.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MessagePayload>> messageListenerContainerKafkaListenerContainerFactory(ConsumerFactory<String, MessagePayload> messagePayloadConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, MessagePayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messagePayloadConsumerFactory);
        return factory;
    }
}
