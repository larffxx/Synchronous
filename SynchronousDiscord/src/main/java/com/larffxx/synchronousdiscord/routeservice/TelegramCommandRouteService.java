package com.larffxx.synchronousdiscord.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.executor.CommandExecutor;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.controller.slashcommand.Command;
import com.larffxx.synchronousdiscord.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramCommandRouteService extends RouteService<Command>{
    private final CommandExecutor executor;

    public TelegramCommandRouteService(EventReceiver eventReceiver, PreProcessor<Command> preProcessor, CommandExecutor executor, ServersConnectRepository serversConnectRepository) {
        super(eventReceiver, preProcessor, serversConnectRepository);
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
        } catch (CommandException e) {
            System.out.println(e.getMessage());
        }
    }
}
