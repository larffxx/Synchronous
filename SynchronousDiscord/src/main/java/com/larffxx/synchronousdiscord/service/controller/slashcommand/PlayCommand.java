package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.DiscordAudioContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;


@Service
public class PlayCommand implements Command {
    private final PlayerManager playerManager;
    private final DiscordContextResolver discordContextResolver;
    private String link;

    public PlayCommand(PlayerManager playerManager, DiscordContextResolver discordContextResolver) {
        this.playerManager = playerManager;
        this.discordContextResolver = discordContextResolver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (!member.getVoiceState().inAudioChannel()) {
            event.reply("You are not in a voice channel").queue();
            return;
        }

        link = event.getOption(CommandConstants.PLAY_LINK_FROM_DISCORD).getAsString();
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        VoiceChannel channel = member.getVoiceState().getChannel().asVoiceChannel();
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);

        playerManager.loadAndPlay(event.getChannel().asTextChannel(), link);
        event.getHook().editOriginal(CommandConstants.PLAY_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        DiscordAudioContext context = discordContextResolver.resolveContext(telegramCommandContext);

        link = String.valueOf(telegramCommandContext.options().get(0));
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }
        Guild guild = context.guild();
        TextChannel textChannel = context.textChannel();

        VoiceChannel channel = guild.getVoiceChannelsByName("General", true).get(0);
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);
        playerManager.loadAndPlay(textChannel, link);
    }

    private boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    @Override
    public String getCommand() {
        return "play";
    }
}
