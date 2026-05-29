package com.larffxx.synchronousdiscord.domain.service;

import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.domain.exception.service.GuildNotFoundException;
import com.larffxx.synchronousdiscord.domain.exception.service.NoConnectionBetweenServersException;
import com.larffxx.synchronousdiscord.domain.exception.service.TextChannelNotFoundException;
import com.larffxx.synchronousdiscord.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.record.DiscordMusicContext;
import com.larffxx.synchronousdiscord.dto.ServersConnectDTO;
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
public class DiscordMusicService {
    private final ServersConnectRepository serversConnectRepository;
    private final ServersConnectMapper serversConnectMapper;
    private final EventReceiver eventReceiver;
    private final ResultHandler resultHandler;

    public DiscordMusicService(ServersConnectRepository repository, ServersConnectMapper mapper, EventReceiver eventReceiver, ResultHandler resultHandler) {
        this.serversConnectRepository = repository;
        this.serversConnectMapper = mapper;
        this.eventReceiver = eventReceiver;
        this.resultHandler = resultHandler;
    }

    public DiscordMusicContext resolveMusicContext(String telegramChatID) {
        String guildID = resolveGuildId(telegramChatID);
        Guild guild = resolveGuild(guildID);
        TextChannel textChannel = resolveTextChannel(guild);
        GuildMusicManager manager = resultHandler.getMusicManager(guild);

        return new DiscordMusicContext(guild, textChannel, manager);
    }

    private static TextChannel resolveTextChannel(Guild guild) {
        List<TextChannel> channels = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true);
        if (channels.isEmpty()) {
            throw new TextChannelNotFoundException(InfExcMessages.TEXT_CHANNEL_NOT_FOUND_EXCEPTION);
        }

        return channels.get(0);
    }

    @NotNull
    private Guild resolveGuild(String guildID) {
        Guild guild = eventReceiver.getJda().getGuildById(guildID);
        if (guild == null) {
            throw new GuildNotFoundException(InfExcMessages.GUILD_NOT_FOUND_EXCEPTION);
        }
        return guild;
    }

    private String resolveGuildId(String telegramChatID) {
        ServersConnect serversConnect = serversConnectRepository.getConnectByTelegramChannel(telegramChatID);

        if (serversConnect == null) {
            throw new NoConnectionBetweenServersException(InfExcMessages.NO_CONNECTION_BETWEEN_SERVERS);
        }
        ServersConnectDTO dto = serversConnectMapper.toDTO(serversConnect);

        return dto.getDiscordGuild();
    }
}
