package com.larffxx.synchronousdiscord.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${dMTopic}")
    private String topic;

    @Value("${dCTopic}")
    private String dCommand;

    @Bean
    public NewTopic dMessage(){
        return TopicBuilder.name(topic)
                .build();
    }

    @Bean
    public NewTopic dCommand(){
        return TopicBuilder.name(dCommand).build();
    }
}
