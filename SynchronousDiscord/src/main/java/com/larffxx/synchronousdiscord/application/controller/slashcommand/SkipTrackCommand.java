package com.larffxx.synchronousdiscord.application.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SkipTrackCommand implements Command{
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;

    public SkipTrackCommand(ResultHandler resultHandler, EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.resultHandler = resultHandler;
        this.eventReceiver = eventReceiver;
        this.serversConnectRepository = serversConnectRepository;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        musicManager.getScheduler().nextTrack();

        event.getHook().editOriginal(CommandConstants.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectRepository.getConnectByTelegramChannel(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);
        TextChannel textChannel = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true).get(0);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);

        musicManager.getScheduler().nextTrack();
        textChannel.sendMessage(CommandConstants.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "skip";
    }


}
