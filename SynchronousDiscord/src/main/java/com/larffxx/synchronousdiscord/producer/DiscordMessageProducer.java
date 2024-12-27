package com.larffxx.synchronousdiscord.producer;

import com.larffxx.synchronousdiscord.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void sendWithAttachment(MessageReceivedEvent event){
        List<net.dv8tion.jda.api.entities.Message.Attachment> attachments = event.getMessage().getAttachments();
        MessagePayload messagePayload = new MessagePayload(event.getAuthor().getName(), attachments.stream().map(net.dv8tion.jda.api.entities.Message.Attachment::getUrl).toList().toString(), event.getGuild().getIdLong());

        Message message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        kafkaTemplate.send(message);
    }
}
