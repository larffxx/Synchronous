package com.larffxx.synchronousdiscord.senders;

import com.larffxx.synchronousdiscord.payload.CommandPayload;
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
    @Value("${dMTopic}")
    private String topic;
    @Value("${dCTopic}")
    private String cTopic;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    private final KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate;
    private final EventReceiver eventReceiver;

    public Sender(KafkaTemplate<String, MessagePayload> kafkaTemplate, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, EventReceiver eventReceiver) {
        this.kafkaTemplate = kafkaTemplate;
        this.commandPayloadKafkaTemplate = commandPayloadKafkaTemplate;
        this.eventReceiver = eventReceiver;
    }

    public abstract void send(T data);

}
