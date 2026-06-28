package com.larffxx.synchronoustelegram.infrastructure.producer;

import com.larffxx.synchronoustelegram.infrastructure.mapper.PayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.mapper.UpdateToMessagePayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.util.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
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
    private final ServersConnectRepository serversConnectRepository;

    public TelegramKafkaMessageProducer(TmpToJpgConverter tmpToJpgConverter, KafkaTemplate<String, MessagePayload> kafkaTemplate, ServersConnectRepository serversConnectRepository) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.kafkaTemplate = kafkaTemplate;
        this.serversConnectRepository = serversConnectRepository;
    }

    public void sendKafkaMessage(MessagePayload messagePayload) {
        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }

    public void sendKafkaMessage(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String guildId = serversConnectRepository.findByTelegramChannel(chatId).getDiscordGuild();

        PayloadMapper<MessagePayload> updateToMessagePayloadMapper = new UpdateToMessagePayloadMapper();
        MessagePayload messagePayload = updateToMessagePayloadMapper.mapToPayload(update, guildId);

        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }
}
