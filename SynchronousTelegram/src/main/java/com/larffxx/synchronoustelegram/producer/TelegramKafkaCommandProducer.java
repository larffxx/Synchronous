package com.larffxx.synchronoustelegram.producer;

import com.larffxx.synchronoustelegram.payload.CommandPayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class TelegramKafkaCommandProducer {
    @Value("${tCTopic}")
    private String topic;
    private final KafkaTemplate<String, CommandPayload> kafkaTemplate;

    public TelegramKafkaCommandProducer(KafkaTemplate<String, CommandPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaMessage(Update update){
        CommandPayload commandPayload = new CommandPayload(update.getMessage().getChatId(), update.getMessage().getFrom().getUserName(), update.getMessage().getText().replace("/", ""),
                new ArrayList<>(List.of(update.getMessage().getText().split(" "))));
        Message message = MessageBuilder.withPayload(commandPayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }
}
