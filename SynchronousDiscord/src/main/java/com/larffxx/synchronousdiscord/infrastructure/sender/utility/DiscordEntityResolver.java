package com.larffxx.synchronousdiscord.infrastructure.sender.utility;

import com.larffxx.synchronousdiscord.domain.exception.service.GuildNotFoundException;
import com.larffxx.synchronousdiscord.domain.exception.service.NoConnectionBetweenServersException;
import com.larffxx.synchronousdiscord.domain.exception.service.TextChannelNotFoundException;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordEntityResolver {
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;

    public DiscordEntityResolver(EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.eventReceiver = eventReceiver;
        this.serversConnectRepository = serversConnectRepository;
    }

    public static TextChannel resolveTextChannel(Guild guild) {
        List<TextChannel> channels = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true);
        if (channels.isEmpty()) {
            throw new TextChannelNotFoundException(InfExcMessages.TEXT_CHANNEL_NOT_FOUND_EXCEPTION);
        }

        return channels.get(0);
    }

    @NotNull
    public Guild resolveGuild(String guildID) {
        Guild guild = eventReceiver.getJda().getGuildById(guildID);
        if (guild == null) {
            throw new GuildNotFoundException(InfExcMessages.GUILD_NOT_FOUND_EXCEPTION);
        }
        return guild;
    }

    public String resolveGuildId(String telegramChatID) {
        ServersConnect serversConnect = serversConnectRepository.getConnectByTelegramChannel(telegramChatID);

        if (serversConnect == null) {
            throw new NoConnectionBetweenServersException(InfExcMessages.NO_CONNECTION_BETWEEN_SERVERS);
        }

        return serversConnect.getDiscordGuild();
    }
}
