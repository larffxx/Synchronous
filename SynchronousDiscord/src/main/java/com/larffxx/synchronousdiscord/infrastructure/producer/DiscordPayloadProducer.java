package com.larffxx.synchronousdiscord.infrastructure.producer;

import com.larffxx.synchronousdiscord.infrastructure.parser.CommandPayloadParser;
import com.larffxx.synchronousdiscord.infrastructure.parser.MessagePayloadParser;
import com.larffxx.synchronousdiscord.infrastructure.payload.CommandPayload;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Discord Payload Producer class.
 */
@Getter
@Setter
@Component
public class DiscordPayloadProducer {
    /**
     * The c topic.
     */
    @Value("${dCTopic}")
    private String cTopic;
    /**
     * The m topic.
     */
    @Value("${dMTopic}")
    private String mTopic;
    /**
     * The command payload kafka template.
     */
    private final KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate;
    /**
     * The message payload kafka template.
     */
    private final KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate;
    /**
     * The command payload parser.
     */
    private final CommandPayloadParser commandPayloadParser;
    /**
     * The message payload parser.
     */
    private final MessagePayloadParser messagePayloadParser;

    /**
     * Creates a new DiscordPayloadProducer.
     * @param commandPayloadKafkaTemplate the command payload kafka template.
     * @param messagePayloadKafkaTemplate the message payload kafka template.
     * @param commandPayloadParser the command payload parser.
     * @param messagePayloadParser the message payload parser.
     */
    public DiscordPayloadProducer(KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate, CommandPayloadParser commandPayloadParser, MessagePayloadParser messagePayloadParser) {
        this.commandPayloadKafkaTemplate = commandPayloadKafkaTemplate;
        this.messagePayloadKafkaTemplate = messagePayloadKafkaTemplate;
        this.commandPayloadParser = commandPayloadParser;
        this.messagePayloadParser = messagePayloadParser;
    }

    /**
     * Sends event.
     * @param event the event.
     */
    public void send(SlashCommandInteractionEvent event) {
        CommandPayload commandPayload = commandPayloadParser.parse(event);

        produceKafkaMessage(commandPayload, cTopic, commandPayloadKafkaTemplate);
    }

    /**
     * Sends event.
     * @param event the event.
     */
    public void send(MessageReceivedEvent event) {
        MessagePayload messagePayload = messagePayloadParser.parse(event);

        produceKafkaMessage(messagePayload,mTopic,messagePayloadKafkaTemplate);
    }

    /**
     * Produces kafka message.
     * @param <T> the T type parameter.
     * @param payload the payload.
     * @param topic the topic.
     * @param kafkaTemplate the kafka template.
     */
    private <T> void produceKafkaMessage(T payload, String topic, KafkaTemplate<String, T> kafkaTemplate) {
        Message<T> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        kafkaTemplate.send(message);
    }
}
