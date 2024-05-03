package com.larffxx.synchronousdiscord.producer;

import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.Sender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DiscordMessageProducer extends Sender<MessageReceivedEvent> {
    public DiscordMessageProducer(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver) {
        super(kafkaTemplate, eventReceiver);
    }

    @Override
    public void send(MessageReceivedEvent event) {
        MessagePayload messagePayload = new MessagePayload(event.getAuthor().getName(), event.getMessage().getContentDisplay(), event.getGuild().getIdLong());
        Message message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, getTopic())
                .build();
        getKafkaTemplate().send(message);
    }
    public void send(MessageReceivedEvent event, String URL){
        MessagePayload messagePayload = new MessagePayload(event.getAuthor().getName(), URL, event.getGuild().getIdLong());
        Message message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, getTopic())
                .build();
        getKafkaTemplate().send(message);
    }
}
