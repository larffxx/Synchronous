package com.larffxx.synchronousdiscord.service.controller.slashcommand;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class StopCommand implements Command{
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;
    private final DiscordContextResolver discordContextResolver;

    public StopCommand(ResultHandler resultHandler, EventReceiver eventReceiver, DiscordContextResolver discordContextResolver) {
        this.resultHandler = resultHandler;
        this.eventReceiver = eventReceiver;
        this.discordContextResolver = discordContextResolver;
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
        DiscordContext context = discordContextResolver.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        TextChannel textChannel = context.textChannel();

        context.guildMusicManager().getScheduler().stopTrack();
        textChannel.sendMessage(CommandConstants.STOP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
