package com.larffxx.synchronousdiscord.producer;

import com.larffxx.synchronousdiscord.parser.CommandPayloadParser;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class DiscordCommandProducer {
    @Value("${dCTopic}")
    private String cTopic;
    private final KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate;
    public DiscordCommandProducer(KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate) {
        this.commandPayloadKafkaTemplate = commandPayloadKafkaTemplate;
    }

    public void send(SlashCommandInteractionEvent event) {
        CommandPayload commandPayload = new CommandPayloadParser().parse(event);

        Message command = MessageBuilder
                .withPayload(commandPayload)
                .setHeader(KafkaHeaders.TOPIC, cTopic)
                .build();

        commandPayloadKafkaTemplate.send(command);
    }


}
