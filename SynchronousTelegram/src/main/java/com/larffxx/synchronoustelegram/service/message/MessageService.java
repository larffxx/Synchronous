package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;

public interface MessageService {
    void send(DiscordPayload discordPayload);

    String getType();
}
