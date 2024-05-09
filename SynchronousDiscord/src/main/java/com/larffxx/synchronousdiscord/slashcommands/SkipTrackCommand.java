package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SkipTrackCommand extends Command{
    private final ServersConnectDAO serversConnectDAO;
    private final ResultHandler resultHandler;

    public SkipTrackCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        musicManager.getScheduler().nextTrack();
        event.reply("Skipped").queue();
    }

    @Override
    public void execute(JsonNode data) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()));
        musicManager.getScheduler().nextTrack();
    }

    @Override
    public String getCommand() {
        return "skip";
    }
}
