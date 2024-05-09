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
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class StopCommand extends Command{
    private final ServersConnectDAO serversConnectDAO;
    private final ResultHandler resultHandler;

    public StopCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(event.getGuild());
        guildMusicManager.getScheduler().stopTrack();
        event.reply("Stopped").queue();
        AudioManager manager = event.getGuild().getAudioManager();
        manager.closeAudioConnection();
    }

    @Override
    public void execute(JsonNode data) {
        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()));
        guildMusicManager.getScheduler().stopTrack();
    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
