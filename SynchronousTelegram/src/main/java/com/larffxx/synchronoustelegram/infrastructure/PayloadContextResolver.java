package com.larffxx.synchronoustelegram.infrastructure;

import com.larffxx.synchronoustelegram.domain.record.PayloadContext;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import org.springframework.stereotype.Component;

@Component
public class PayloadContextResolver {
    private final ServersConnectRepository serversConnectRepository;

    public PayloadContextResolver(ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
    }

    public PayloadContext resolvePayloadContext(DiscordPayload discordPayload){
        boolean hasMessage = discordPayload.getMessage() != null && !discordPayload.getMessage().equals("null");
        String caption = hasMessage ? discordPayload.getMessage() : "";
        String chatId = serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel();

        return new PayloadContext(chatId, caption, discordPayload.getFiles());
    }
}
