package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;

public interface Sender {
    void send(DiscordPayload discordPayload);
}
