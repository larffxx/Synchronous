package com.larffxx.synchronoustelegram.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Spring configuration that declares the Kafka topics used by the Telegram module.
 * Declares one topic for synced messages and one for synced commands.
 */
@Configuration
public class KafkaTopicConfig {
    /**
     * Name of the Kafka topic for outgoing Telegram messages.
     */
    @Value("${tMTopic}")
    private String topic;
    /**
     * Name of the Kafka topic for outgoing Telegram commands.
     */
    @Value("${tCTopic}")
    private String tCommand;

    /**
     * Declares the Kafka topic for Telegram messages.
     * @return the message topic definition
     */
    @Bean
    public NewTopic tMessage() {
        return TopicBuilder.name(topic).build();
    }
    /**
     * Declares the Kafka topic for Telegram commands.
     * @return the command topic definition
     */
    @Bean
    public NewTopic tCommand(){
        return TopicBuilder.name(tCommand).build();
    }
}
