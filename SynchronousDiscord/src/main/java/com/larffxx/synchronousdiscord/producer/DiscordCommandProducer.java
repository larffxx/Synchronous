package com.larffxx.synchronousdiscord.producer;

import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.Sender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class DiscordCommandProducer extends Sender<SlashCommandInteractionEvent> {
    public DiscordCommandProducer(KafkaTemplate<String, MessagePayload> kafkaTemplate, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, EventReceiver eventReceiver) {
        super(kafkaTemplate, commandPayloadKafkaTemplate, eventReceiver);
    }

    @Override
    public void send(SlashCommandInteractionEvent event) {
        CommandPayload commandPayload = new CommandPayload(event.getInteraction().getMember().getIdLong(), event.getInteraction().getMember().getEffectiveName(), event.getName(),
                new ArrayList<>(event.getOptions().stream().map(OptionMapping::getAsString).collect(Collectors.toList())));
        Message command = MessageBuilder
                .withPayload(commandPayload)
                .setHeader(KafkaHeaders.TOPIC, getTopic())
                .build();
        getKafkaTemplate().send(command);
    }
}
