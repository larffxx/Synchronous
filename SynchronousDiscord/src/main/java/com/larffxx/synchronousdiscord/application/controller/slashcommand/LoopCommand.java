package com.larffxx.synchronousdiscord.application.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.embed.EmbedSender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoopCommand implements Command {
    private final EmbedSender embedSender;
    private final ServersConnectRepository serversConnectRepository;
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;

    public LoopCommand(EmbedSender embedSender, ResultHandler resultHandler, ServersConnectRepository serversConnectRepository, EventReceiver eventReceiver) {
        this.embedSender = embedSender;
        this.resultHandler = resultHandler;
        this.serversConnectRepository = serversConnectRepository;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        if(musicManager == null || musicManager.getAudioPlayer().getPlayingTrack() == null){
            event.getHook().editOriginal(CommandConstants.LOOP_UNSUCCESSFUL_MESSAGE).queue();
        }else {
            boolean loop = !musicManager.getScheduler().isRepeat();
            musicManager.getScheduler().setRepeat(loop);
            event.getHook().editOriginal(CommandConstants.LOOP_SUCCESS_MESSAGE).queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectRepository.getConnectByTelegramChannel(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);
        EmbedBuilder eb = new EmbedBuilder();

        if(musicManager == null || musicManager.getAudioPlayer().getPlayingTrack() == null){
            eb.setDescription(CommandConstants.LOOP_UNSUCCESSFUL_MESSAGE);
            embedSender.send(eb);
        }else {
            boolean loop = !musicManager.getScheduler().isRepeat();
            musicManager.getScheduler().setRepeat(loop);
            eb.setDescription(CommandConstants.LOOP_SUCCESS_MESSAGE);
            embedSender.send(eb);
        }
    }

    @Override
    public String getCommand() {
        return "loop";
    }


}
