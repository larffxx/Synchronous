package com.larffxx.synchronoustelegram.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${tMTopic}")
    private String topic;
    @Value("${tCTopic}")
    private String tCommand;

    @Bean
    public NewTopic tMessage() {
        return TopicBuilder.name(topic).build();
    }
    @Bean
    public NewTopic tCommand(){
        return TopicBuilder.name(tCommand).build();
    }
}
