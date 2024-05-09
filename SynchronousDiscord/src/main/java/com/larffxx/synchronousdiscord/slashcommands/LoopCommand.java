package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoopCommand extends Command {
    private final ServersConnectDAO serversConnectDAO;
    private final CommandListener commandListener;
    private final ResultHandler resultHandler;

    public LoopCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, CommandListener commandListener, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.commandListener = commandListener;
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(t.getGuild());
        if(musicManager == null || musicManager.getAudioPlayer().getPlayingTrack() == null){
            t.reply("No music playing").queue();
        }else {
            boolean loop = !musicManager.getScheduler().isRepeat();
            musicManager.getScheduler().setRepeat(loop);
            t.reply("Looped").queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()));
        EmbedBuilder eb = new EmbedBuilder();
        if(musicManager == null || musicManager.getAudioPlayer().getPlayingTrack() == null){
            eb.setDescription("No music is playing");
            commandListener.getEmbedSender().send(eb);
        }else {
            boolean loop = !musicManager.getScheduler().isRepeat();
            musicManager.getScheduler().setRepeat(loop);
            eb.setDescription("Looped");
            commandListener.getEmbedSender().send(eb);
        }
    }

    @Override
    public String getCommand() {
        return "loop";
    }
}
