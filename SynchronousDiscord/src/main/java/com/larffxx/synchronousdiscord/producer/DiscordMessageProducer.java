package com.larffxx.synchronousdiscord.producer;

import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.Sender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DiscordMessageProducer {
    @Value("${dMTopic}")
    private String topic;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    public DiscordMessageProducer(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MessageReceivedEvent event) {
        MessagePayload messagePayload = new MessagePayload(event.getAuthor().getName(), event.getMessage().getContentDisplay(), event.getGuild().getIdLong());
        Message message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        kafkaTemplate.send(message);
    }

    public void send(MessageReceivedEvent event, String URL){
        MessagePayload messagePayload = new MessagePayload(event.getAuthor().getName(), URL, event.getGuild().getIdLong());
        Message message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        kafkaTemplate.send(message);
    }
}
