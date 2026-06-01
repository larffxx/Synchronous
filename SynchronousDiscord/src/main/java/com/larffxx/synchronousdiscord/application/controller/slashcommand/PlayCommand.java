package com.larffxx.synchronousdiscord.application.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.domain.service.DiscordContextResolveService;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;


@Component
public class PlayCommand implements Command {
    private final PlayerManager playerManager;
    private final DiscordContextResolveService discordContextResolveService;
    private String link;

    public PlayCommand(PlayerManager playerManager, DiscordContextResolveService discordContextResolveService) {
        this.playerManager = playerManager;
        this.discordContextResolveService = discordContextResolveService;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (member.getVoiceState().getChannel().asVoiceChannel() == null) {
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
    public void execute(JsonNode data) {
        DiscordContext context = discordContextResolveService.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());

        link = String.valueOf(data.findValues(CommandConstants.PLAY_LINK_FROM_TELEGRAM).get(0).get(0).asText());
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
