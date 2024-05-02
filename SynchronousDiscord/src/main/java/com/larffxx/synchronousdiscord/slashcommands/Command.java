package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class Command implements CommandInterface {
    @Value("${dTopic}")
    private String topic;
    private final EventReceiver eventReceiver;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    public Command(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventReceiver = eventReceiver;
    }
}
