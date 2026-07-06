package com.larffxx.synchronousdiscord.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessageType;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

@Component
public class JsonNodeToTelegramMessageContextContextMapper implements ContextMapper<TelegramMessageContext> {
    private final ServersConnectRepository serversConnectRepository;
    private final EventContext eventContext;

    public JsonNodeToTelegramMessageContextContextMapper(ServersConnectRepository serversConnectRepository, EventContext eventContext) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventContext = eventContext;
    }

    public TelegramMessageContext toContext(JsonNode jsonNode) {
        String telegramChatID = jsonNode.get(CommandConstants.TELEGRAM_CHAT_ID).asText();
        String guildID = serversConnectRepository.getConnectByTelegramChannel(telegramChatID).getDiscordGuild();
        String messageType = jsonNode.findValue(SendersConstants.MESSAGE_TYPE).asText();
        String message = jsonNode.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText();
        String telegramUsername = jsonNode.findValue(SendersConstants.NAME_IN_TELEGRAM).asText();
        String file = jsonNode.findValue(SendersConstants.FILE_FROM_TELEGRAM).asText();
        Guild guild = eventContext.getJda().getGuildById(guildID);
        TextChannel telegramChannel = guild
                .getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL,true)
                .get(0);

        return new TelegramMessageContext(guild,file,message,telegramUsername,telegramChatID, MessageType.valueOf(messageType),telegramChannel);
    }
}
