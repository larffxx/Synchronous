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


@Getter
@Setter
@Component
public class DiscordPayloadProducer {
    @Value("${dCTopic}")
    private String cTopic;
    @Value("${dMTopic}")
    private String mTopic;
    private final KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate;
    private final KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate;

    public DiscordPayloadProducer(KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate) {
        this.commandPayloadKafkaTemplate = commandPayloadKafkaTemplate;
        this.messagePayloadKafkaTemplate = messagePayloadKafkaTemplate;
    }


    //TODO: produce with ServersConnectPayload with sending ids of telegram chat and guild id
    public void send(SlashCommandInteractionEvent event) {
        produceKafkaMessage(new CommandPayloadParser().parse(event), cTopic, commandPayloadKafkaTemplate);
    }

    //TODO: produce with ServersConnectPayload with sending ids of telegram chat and guild id
    public void send(MessageReceivedEvent event) {
        MessagePayloadParser messagePayloadParser = new MessagePayloadParser();

        MessagePayload messagePayload = messagePayloadParser.parse(event);

        produceKafkaMessage(messagePayload,mTopic,messagePayloadKafkaTemplate);
    }

    private <T> void produceKafkaMessage(T payload, String topic, KafkaTemplate<String, T> kafkaTemplate) {
        Message<T> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        kafkaTemplate.send(message);
    }
}
