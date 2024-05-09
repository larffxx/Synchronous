package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class Command implements CommandInterface {
    @Value("${dMTopic}")
    private String topic;
    @Value("${dCTopic}")
    private String cTopic;
    private final EventReceiver eventReceiver;
    private final KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate;
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    public Command(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventReceiver = eventReceiver;
        this.commandPayloadKafkaTemplate = commandPayloadKafkaTemplate;
    }

}
