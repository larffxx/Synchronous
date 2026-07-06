package com.larffxx.synchronousdiscord.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class JsonNodeToTelegramCommandContextContextMapper implements ContextMapper<TelegramCommandContext> {
    private final ServersConnectRepository serversConnectRepository;
    private final EventContext eventContext;

    public JsonNodeToTelegramCommandContextContextMapper(ServersConnectRepository serversConnectRepository, EventContext eventContext) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventContext = eventContext;
    }

    public TelegramCommandContext toContext(JsonNode node) {
        String telegramChatID = node.get(CommandConstants.TELEGRAM_CHAT_ID).asText();
        String guildID = serversConnectRepository.getConnectByTelegramChannel(telegramChatID).getDiscordGuild();
        String telegramUsername = node.findValue(SendersConstants.NAME_IN_TELEGRAM).asText();
        String command = node.findValue(CommandConstants.COMMAND_VALUE).asText();
        Guild guild = eventContext.getJda().getGuildById(guildID);
        TextChannel telegramChannel = guild
                .getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL,true)
                .get(0);

        @Nullable List<String> providedOptions = StreamSupport
                .stream(node.get(CommandConstants.COMMAND_OPTIONS).spliterator(), false)
                .map(JsonNode::asText)
                .filter(s -> s != null && !s.isBlank())
                .toList();

        return new TelegramCommandContext(command, telegramUsername, telegramChatID, providedOptions, telegramChannel, guild);
    }
}
