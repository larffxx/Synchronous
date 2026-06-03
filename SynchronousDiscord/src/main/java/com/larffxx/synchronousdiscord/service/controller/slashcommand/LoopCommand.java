package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.infrastructure.sender.embed.EmbedSender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class LoopCommand implements Command {
    private final EmbedSender embedSender;
    private final ResultHandler resultHandler;
    private final DiscordContextResolver discordContextResolver;

    public LoopCommand(EmbedSender embedSender, ResultHandler resultHandler, DiscordContextResolver discordContextResolver) {
        this.embedSender = embedSender;
        this.resultHandler = resultHandler;
        this.discordContextResolver = discordContextResolver;
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
        DiscordContext context = discordContextResolver.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        GuildMusicManager musicManager = context.guildMusicManager();

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
