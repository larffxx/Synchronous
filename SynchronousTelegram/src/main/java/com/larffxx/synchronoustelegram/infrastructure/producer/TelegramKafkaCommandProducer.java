package com.larffxx.synchronoustelegram.infrastructure.producer;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.mapper.PayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.mapper.UpdateToCommandPayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;


/**
 * Publishes command payloads to the Telegram command Kafka topic.
 * Maps Telegram updates or button contexts to command payloads before sending.
 */
@Component
@Getter
@Setter
public class TelegramKafkaCommandProducer {
    /**
     * Name of the Kafka topic for outgoing commands.
     */
    @Value("${tCTopic}")
    private String topic;
    /**
     * Kafka template used to send command payloads.
     */
    private final KafkaTemplate<String, CommandPayload> kafkaTemplate;
    /**
     * Repository used to resolve the Discord guild linked to a Telegram channel.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates the producer with its collaborators.
     * @param kafkaTemplate template for sending command payloads
     * @param serversConnectRepository repository for server links
     */
    public TelegramKafkaCommandProducer(KafkaTemplate<String, CommandPayload> kafkaTemplate, ServersConnectRepository serversConnectRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Maps a command update to a payload and sends it to Kafka.
     * @param update the Telegram update holding the command
     */
    public void sendKafkaMessage(Update update) {
        PayloadMapper<CommandPayload> mapper = new UpdateToCommandPayloadMapper();

        String chatId = update.getMessage().getChatId().toString();
        ServersConnect serversConnect = serversConnectRepository.findByTelegramChannel(chatId);
        if (serversConnect == null) {
            return;
        }
        String guildId = serversConnect.getDiscordGuild();
        CommandPayload commandPayload = mapper.mapToPayload(update, guildId);
        Message message = MessageBuilder.withPayload(commandPayload).setHeader("kafka_topic", topic).build();

        kafkaTemplate.send(message);
    }

    /**
     * Builds a command payload from a button context and sends it to Kafka, skipping unlinked chats.
     * @param buttonContext the command context produced by a button press
     */
    public void sendKafkaMessage(CommandContext buttonContext) {
        String chatId = String.valueOf(buttonContext.chatId());
        ServersConnect connect = serversConnectRepository.findByTelegramChannel(chatId);
        if (connect == null) {
            return;
        }
        CommandPayload commandPayload = new CommandPayload(
                chatId,
                connect.getDiscordGuild(),
                buttonContext.executedBy(),
                buttonContext.commandName(),
                new ArrayList<>(buttonContext.options())
        );
        Message message = MessageBuilder.withPayload(commandPayload).setHeader("kafka_topic", topic).build();

        kafkaTemplate.send(message);
    }
}
