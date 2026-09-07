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

/**
 * Json Node To Telegram Message Context Context Mapper class.
 */
@Component
public class JsonNodeToTelegramMessageContextContextMapper implements ContextMapper<TelegramMessageContext> {
    /**
     * The servers connect repository.
     */
    private final ServersConnectRepository serversConnectRepository;
    /**
     * The event context.
     */
    private final EventContext eventContext;

    /**
     * Creates a new JsonNodeToTelegramMessageContextContextMapper.
     * @param serversConnectRepository the servers connect repository.
     * @param eventContext the event context.
     */
    public JsonNodeToTelegramMessageContextContextMapper(ServersConnectRepository serversConnectRepository, EventContext eventContext) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventContext = eventContext;
    }

    /**
     * Converts JSON node to context object.
     * @param jsonNode the json node.
     * @return the resulting telegram message context.
     */
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
