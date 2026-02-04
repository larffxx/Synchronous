package com.larffxx.synchronousdiscord.application.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
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
public class ConnectCommand implements Command{
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;

    public ConnectCommand(EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        ServersConnect serversConnect = new ServersConnect(event.getGuild().getId(), event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString());

        serversConnectRepository.save(serversConnect);
        event.getHook().editOriginal(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        ServersConnect serversConnect = serversConnectRepository.getConnectByTelegramChannel(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        Guild guildId = eventReceiver.getJda().getGuildById(serversConnect.getDiscordGuild());
        TextChannel textChannel = guildId.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL,true).get(0);

        textChannel.sendMessage(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
