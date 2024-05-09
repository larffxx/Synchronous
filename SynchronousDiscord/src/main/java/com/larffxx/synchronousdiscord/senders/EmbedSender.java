package com.larffxx.synchronousdiscord.senders;

import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmbedSender extends Sender<EmbedBuilder> {
    public EmbedSender(KafkaTemplate<String, MessagePayload> kafkaTemplate, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, EventReceiver eventReceiver) {
        super(kafkaTemplate, commandPayloadKafkaTemplate, eventReceiver);
    }

    @Override
    public void send(EmbedBuilder eb) {
        getEventReceiver().getTextChannel().sendMessageEmbeds(eb.build()).queue();
    }
}
