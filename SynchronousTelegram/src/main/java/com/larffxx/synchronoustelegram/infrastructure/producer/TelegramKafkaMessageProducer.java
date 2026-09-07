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

/**
 * Publishes message payloads to the Telegram message Kafka topic.
 * Sends already built payloads or maps Telegram updates to payloads before sending.
 */
@Component
@Getter
@Setter
public class TelegramKafkaMessageProducer {
    /**
     * Name of the Kafka topic for outgoing messages.
     */
    @Value("${tMTopic}")
    private String topic;
    /**
     * Converter that turns downloaded photos into JPG files.
     */
    private final TmpToJpgConverter tmpToJpgConverter;
    /**
     * Kafka template used to send message payloads.
     */
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    /**
     * Repository used to resolve the Discord guild linked to a Telegram channel.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates the producer with its collaborators.
     * @param tmpToJpgConverter converter for downloaded photos
     * @param kafkaTemplate template for sending message payloads
     * @param serversConnectRepository repository for server links
     */
    public TelegramKafkaMessageProducer(TmpToJpgConverter tmpToJpgConverter, KafkaTemplate<String, MessagePayload> kafkaTemplate, ServersConnectRepository serversConnectRepository) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.kafkaTemplate = kafkaTemplate;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Sends an already built message payload to Kafka.
     * @param messagePayload the payload to send
     */
    public void sendKafkaMessage(MessagePayload messagePayload) {
        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }

    /**
     * Maps a text update to a message payload and sends it to Kafka.
     * @param update the Telegram update holding the message
     */
    public void sendKafkaMessage(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String guildId = serversConnectRepository.findByTelegramChannel(chatId).getDiscordGuild();

        PayloadMapper<MessagePayload> updateToMessagePayloadMapper = new UpdateToMessagePayloadMapper();
        MessagePayload messagePayload = updateToMessagePayloadMapper.mapToPayload(update, guildId);

        Message message = MessageBuilder.withPayload(messagePayload).setHeader("kafka_topic", topic).build();
        kafkaTemplate.send(message);
    }
}
