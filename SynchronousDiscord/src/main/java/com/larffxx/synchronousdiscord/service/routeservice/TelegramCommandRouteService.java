package com.larffxx.synchronousdiscord.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.service.executor.CommandExecutor;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramCommandRouteService extends RouteService{
    private final CommandExecutor executor;

    public TelegramCommandRouteService(EventReceiver eventReceiver, CommandExecutor executor, ServersConnectRepository serversConnectRepository) {
        super(eventReceiver,  serversConnectRepository);
        this.executor = executor;
    }

    public void send(JsonNode data){
        String telegramChatID = data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText();
        String guildId = getServersConnectRepository().getConnectByTelegramChannel(telegramChatID).getDiscordGuild();
        TextChannel telegramChannel = getEventReceiver()
                .getJda()
                .getGuildById(guildId)
                .getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL,true)
                .get(0);

        getEventReceiver().setTextChannel(telegramChannel);

        try {
            executor.execute(data);
        } catch (RouteServiceException e) {
            throw new RouteServiceException(InfExcMessages.ROUTE_SERVICE_EXCEPTION);
        }
    }
}
