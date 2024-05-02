package com.larffxx.synchronousdiscord.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.senders.MessageSenderFromKafkaSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;



@Component
@Getter
@Setter
public class KafkaListeners {
    private final MessageSenderFromKafkaSender messageSenderFromKafkaSender;

    public KafkaListeners(MessageSenderFromKafkaSender messageSenderFromKafkaSender) {
        this.messageSenderFromKafkaSender = messageSenderFromKafkaSender;
    }

    @KafkaListener(topics = "${tTopic}", groupId = "${groupId}")
    public void listener(@Payload String user) {
        JsonNode data = null;
        try {
            data = new ObjectMapper().readTree(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messageSenderFromKafkaSender.send(data);
    }
}
