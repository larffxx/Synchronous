package com.larffxx.synchronoustelegram.infrastructure.producer;

import com.larffxx.synchronoustelegram.application.handler.utility.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import com.larffxx.synchronoustelegram.util.constant.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
@Setter
public class TelegramKafkaMessageProducer {
    @Value("${tMTopic}")
    private String topic;
    private final TmpToJpgConverter tmpToJpgConverter;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    public TelegramKafkaMessageProducer(TmpToJpgConverter tmpToJpgConverter, KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaMessage(MessagePayload messagePayload) {
        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }

    public void sendKafkaMessage(Update update) {
        MessagePayload messagePayload = new MessagePayload(update.getMessage().getFrom().getUserName(), update.getMessage().getText(), update.getMessage().getChatId(), MessageType.chatMessage);
        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }
}
