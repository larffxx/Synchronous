package com.larffxx.synchronousdiscord.senders;

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
public abstract class Sender <T> {
    @Value("${dTopic}")
    private String topic;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    private final EventReceiver eventReceiver;

    public Sender(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventReceiver = eventReceiver;
    }

    public abstract void send(T data);

}
