package com.larffxx.synchronousdiscord.slashcommands;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
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
public class StopCommand extends Command{
    private final ServersConnectDAO serversConnectDAO;
    private final ResultHandler resultHandler;

    public StopCommand(EventReceiver eventReceiver, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(event.getGuild());

        guildMusicManager.getScheduler().stopTrack();
        event.reply(CommandInfMessages.STOP_SUCCESS_MESSAGE).queue();


        AudioManager manager = event.getGuild().getAudioManager();
        manager.closeAudioConnection();
    }

    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(CommandInfMessages.GUILD_ID_FROM_TELEGRAM).asText()).getDiscordGuild();
        Guild guild = getEventReceiver().getJda().getGuildById(guildId);
        TextChannel textChannel = guild.getTextChannelsByName("telegram", true).get(0);

        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(guild);

        guildMusicManager.getScheduler().stopTrack();
        textChannel.sendMessage(CommandInfMessages.STOP_SUCCESS_MESSAGE).queue();

    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
