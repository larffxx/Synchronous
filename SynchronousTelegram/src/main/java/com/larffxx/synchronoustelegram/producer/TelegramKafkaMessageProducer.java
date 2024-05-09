package com.larffxx.synchronoustelegram.producer;

import com.larffxx.synchronoustelegram.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Component
@Getter
@Setter
public class TelegramKafkaMessageProducer {
    @Value("${tMTopic}")
    private String topic;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    public TelegramKafkaMessageProducer(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaMessage(Update update, File file) {
        MessagePayload messagePayload;
        if (update.getMessage().getCaption() != null) {
            messagePayload = new MessagePayload(update.getMessage().getFrom().getUserName(), update.getMessage().getCaption(), file.getAbsoluteFile(), update.getMessage().getChatId());
        } else {
            messagePayload = new MessagePayload(update.getMessage().getFrom().getUserName(), file.getAbsoluteFile(), update.getMessage().getChatId());
        }

        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }

    public void sendKafkaMessage(Update update) {
        MessagePayload messagePayload = new MessagePayload(update.getMessage().getFrom().getUserName(), update.getMessage().getText(), update.getMessage().getChatId());
        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }
}
