package com.larffxx.synchronousdiscord.service.controller.slashcommand;


import com.larffxx.synchronousdiscord.domain.context.DiscordAudioContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
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
    private final EventContext eventContext;
    private final DiscordContextResolver discordContextResolver;

    public StopCommand(ResultHandler resultHandler, EventContext eventContext, DiscordContextResolver discordContextResolver) {
        this.resultHandler = resultHandler;
        this.eventContext = eventContext;
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
    public void execute(TelegramCommandContext telegramCommandContext) {
        DiscordAudioContext context = discordContextResolver.resolveContext(telegramCommandContext);
        TextChannel textChannel = context.textChannel();

        context.guildMusicManager().getScheduler().stopTrack();
        textChannel.sendMessage(CommandConstants.STOP_SUCCESS_MESSAGE).queue();
        context.guild().getAudioManager().closeAudioConnection();
    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
