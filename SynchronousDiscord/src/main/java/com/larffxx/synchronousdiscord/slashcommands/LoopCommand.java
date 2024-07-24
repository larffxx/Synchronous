package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.InfMessages;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoopCommand extends Command {
    private final ServersConnectDAO serversConnectDAO;
    private final CommandListener commandListener;
    private final ResultHandler resultHandler;

    public LoopCommand(EventReceiver eventReceiver, CommandListener commandListener, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.commandListener = commandListener;
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(t.getGuild());
        if(musicManager == null || musicManager.getAudioPlayer().getPlayingTrack() == null){
            t.reply(InfMessages.LOOP_UNSUCCESSFUL_MESSAGE).queue();
        }else {
            boolean loop = !musicManager.getScheduler().isRepeat();
            musicManager.getScheduler().setRepeat(loop);
            t.reply(InfMessages.LOOP_SUCCESS_MESSAGE).queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(InfMessages.GUILD_ID_FROM_TELEGRAM).asText()).getDiscordGuild();
        Guild guild = getEventReceiver().getJda().getGuildById(guildId);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);
        EmbedBuilder eb = new EmbedBuilder();

        if(musicManager == null || musicManager.getAudioPlayer().getPlayingTrack() == null){
            eb.setDescription(InfMessages.LOOP_UNSUCCESSFUL_MESSAGE);
            commandListener.getEmbedSender().send(eb);
        }else {
            boolean loop = !musicManager.getScheduler().isRepeat();
            musicManager.getScheduler().setRepeat(loop);
            eb.setDescription(InfMessages.LOOP_SUCCESS_MESSAGE);
            commandListener.getEmbedSender().send(eb);
        }
    }

    @Override
    public String getCommand() {
        return "loop";
    }


}
