package com.larffxx.synchronousdiscord.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka Topic Config class.
 */
@Configuration
public class KafkaTopicConfig {
    /**
     * The topic.
     */
    @Value("${dMTopic}")
    private String topic;

    /**
     * The d command.
     */
    @Value("${dCTopic}")
    private String dCommand;

    /**
     * Creates d message bean.
     * @return the resulting new topic.
     */
    @Bean
    public NewTopic dMessage(){
        return TopicBuilder.name(topic)
                .build();
    }

    /**
     * Creates d command bean.
     * @return the resulting new topic.
     */
    @Bean
    public NewTopic dCommand(){
        return TopicBuilder.name(dCommand).build();
    }
}
