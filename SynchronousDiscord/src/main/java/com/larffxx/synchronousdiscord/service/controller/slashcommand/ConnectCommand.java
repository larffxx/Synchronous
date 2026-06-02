package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronousdiscord.domain.dto.ServersConnectDTO;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
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
    private final ServersConnectMapper serversConnectMapper;

    public ConnectCommand(EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository, ServersConnectMapper serversConnectMapper) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventReceiver = eventReceiver;
        this.serversConnectMapper = serversConnectMapper;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        ServersConnectDTO dto = new ServersConnectDTO(
                event.getGuild().getId(),
                event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString()
        );

        ServersConnect serversConnect = serversConnectMapper.toEntity(dto);

        serversConnectRepository.save(serversConnect);
        event.getHook().editOriginal(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode telegramPayload) {
        String telegramId = telegramPayload.get(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).asText();
        ServersConnect serversConnect = serversConnectRepository.getConnectByTelegramChannel(telegramId);
        ServersConnectDTO dto = serversConnectMapper.toDTO(serversConnect);

        Guild guild = eventReceiver.getJda().getGuildById(dto.getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL,true).get(0);

        textChannel.sendMessage(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
