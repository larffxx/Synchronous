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
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class StopCommand implements Command{
    private final ServersConnectRepository serversConnectRepository;
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;

    public StopCommand(ResultHandler resultHandler, ServersConnectRepository serversConnectRepository, EventReceiver eventReceiver) {
        this.resultHandler = resultHandler;
        this.serversConnectRepository = serversConnectRepository;
        this.eventReceiver = eventReceiver;
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
        String guildId = serversConnectRepository.getConnectByTelegramChannel(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);
        TextChannel textChannel = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true).get(0);

        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(guild);

        guildMusicManager.getScheduler().stopTrack();
        textChannel.sendMessage(CommandConstants.STOP_SUCCESS_MESSAGE).queue();

    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
