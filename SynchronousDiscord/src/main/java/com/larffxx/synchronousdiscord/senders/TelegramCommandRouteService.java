package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import com.larffxx.synchronousdiscord.slashcommands.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramCommandRouteService extends Sender<JsonNode> {
    private final CommandPreProcessor commandPreProcessor;

    public TelegramCommandRouteService(KafkaTemplate<String, MessagePayload> kafkaTemplate, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, EventReceiver eventReceiver, CommandPreProcessor commandPreProcessor) {
        super(kafkaTemplate, commandPayloadKafkaTemplate, eventReceiver);
        this.commandPreProcessor = commandPreProcessor;
    }


    @Override
    public void send(JsonNode data) {
        Command command = commandPreProcessor.getCommand(data.findValue("command").asText());
        command.execute(data);
    }
}
