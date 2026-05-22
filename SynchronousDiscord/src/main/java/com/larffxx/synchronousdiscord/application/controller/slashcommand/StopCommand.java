package com.larffxx.synchronousdiscord.application.controller.slashcommand;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.record.DiscordMusicContext;
import com.larffxx.synchronousdiscord.domain.service.DiscordMusicService;
import com.larffxx.synchronousdiscord.dto.ServersConnectDTO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class StopCommand implements Command{
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;
    private final DiscordMusicService discordMusicService;

    public StopCommand(ResultHandler resultHandler, EventReceiver eventReceiver, DiscordMusicService discordMusicService) {
        this.resultHandler = resultHandler;
        this.eventReceiver = eventReceiver;
        this.discordMusicService = discordMusicService;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(event.getGuild());

        guildMusicManager.getScheduler().stopTrack();
        event.getHook().editOriginal(CommandConstants.STOP_SUCCESS_MESSAGE).queue();


        AudioManager manager = event.getGuild().getAudioManager();
        manager.closeAudioConnection();
    }

    @Override
    public void execute(JsonNode data) {
        DiscordMusicContext context = discordMusicService.resolveMusicContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        TextChannel textChannel = context.textChannel();

        context.guildMusicManager().getScheduler().stopTrack();
        textChannel.sendMessage(CommandConstants.STOP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
