package com.larffxx.synchronoustelegram.listeners;

import com.larffxx.synchronoustelegram.commands.SendText;
import com.larffxx.synchronoustelegram.payload.MessagePayload;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
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
public class KafkaListeners {
    @Value("${tTopic}")
    private String topic;
    private String data;
    private final SendText sendText;
    private final UpdateReceiver updateReceiver;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    public KafkaListeners(UpdateReceiver updateReceiver, SendText sendText, KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.updateReceiver = updateReceiver;
        this.sendText = sendText;
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

    public void sendKafkaMessage(Update update, String command) {
        MessagePayload messagePayload = new MessagePayload(update.getMessage().getFrom().getUserName(), command, update.getMessage().getText(), update.getMessage().getChatId());
        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }

}
