package com.larffxx.synchronousweb.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Enables Kafka listener endpoints for the web chat module.
 */
@Configuration
@EnableKafka
public class ChatKafkaConfig {
}
