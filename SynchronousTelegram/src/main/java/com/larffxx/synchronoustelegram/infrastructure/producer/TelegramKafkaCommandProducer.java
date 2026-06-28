package com.larffxx.synchronoustelegram.infrastructure.producer;

import com.larffxx.synchronoustelegram.infrastructure.mapper.PayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.mapper.UpdateToCommandPayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
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
public class TelegramKafkaCommandProducer {
    @Value("${tCTopic}")
    private String topic;
    private final KafkaTemplate<String, CommandPayload> kafkaTemplate;
    private final ServersConnectRepository serversConnectRepository;

    public TelegramKafkaCommandProducer(KafkaTemplate<String, CommandPayload> kafkaTemplate, ServersConnectRepository serversConnectRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.serversConnectRepository = serversConnectRepository;
    }

    public void sendKafkaMessage(Update update) {
        PayloadMapper<CommandPayload> mapper = new UpdateToCommandPayloadMapper();

        String chatId = update.getMessage().getChatId().toString();
        String guildId = serversConnectRepository.findByTelegramChannel(chatId).getDiscordGuild();
        CommandPayload commandPayload = mapper.mapToPayload(update, guildId);
        Message message = MessageBuilder.withPayload(commandPayload).setHeader("kafka_topic", topic).build();

        kafkaTemplate.send(message);
    }
}
