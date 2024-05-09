package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ConnectCommand extends Command{
    private final ServersConnectDAO serversConnectDAO;

    public ConnectCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, ServersConnectDAO serversConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        serversConnectDAO.saveServer(t.getGuild().getId(), t.getOption("telegram").getAsString());
        t.reply("Servers connected").queue();
    }

    @Override
    public void execute(JsonNode data) {
        getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0)
                .sendMessage("Servers Connected").queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
