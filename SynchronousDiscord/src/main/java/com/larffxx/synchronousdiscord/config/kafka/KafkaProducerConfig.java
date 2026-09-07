package com.larffxx.synchronousdiscord.config.kafka;

import com.larffxx.synchronousdiscord.infrastructure.payload.CommandPayload;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import org.apache.kafka.clients.producer.ProducerConfig;
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
 * Kafka Producer Config class.
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * The bootstrap servers.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Producer Config.
     * @return the resulting object.
     */
    public Map<String, Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Creates producer factory bean.
     * @return the resulting message payload.
     */
    @Bean
    public ProducerFactory<String, MessagePayload> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Creates command payload producer factory bean.
     * @return the resulting command payload.
     */
    @Bean
    public ProducerFactory<String, CommandPayload> commandPayloadProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Creates message payload kafka template bean.
     * @param producerFactory the producer factory.
     * @return the resulting message payload.
     */
    @Bean
    public KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate(ProducerFactory<String, MessagePayload> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Creates command payload kafka template bean.
     * @param commandPayloadProducerFactory the command payload producer factory.
     * @return the resulting command payload.
     */
    @Bean
    public KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate(ProducerFactory<String, CommandPayload> commandPayloadProducerFactory){
        return new KafkaTemplate<>(commandPayloadProducerFactory);
    }
}
