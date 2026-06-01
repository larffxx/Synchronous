package com.larffxx.synchronousdiscord.application.controller.slashcommand;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.domain.service.DiscordContextResolveService;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import lombok.Getter;
import lombok.Setter;
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
    private final DiscordContextResolveService discordContextResolveService;

    public StopCommand(ResultHandler resultHandler, EventReceiver eventReceiver, DiscordContextResolveService discordContextResolveService) {
        this.resultHandler = resultHandler;
        this.eventReceiver = eventReceiver;
        this.discordContextResolveService = discordContextResolveService;
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
        DiscordContext context = discordContextResolveService.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        TextChannel textChannel = context.textChannel();

        context.guildMusicManager().getScheduler().stopTrack();
        textChannel.sendMessage(CommandConstants.STOP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
