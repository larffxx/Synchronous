package com.larffxx.synchronoustelegram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for a Discord to Telegram server link.
 * Carries the Discord guild and Telegram channel identifiers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServersConnectDTO {
    /**
     * Identifier of the linked Discord guild.
     */
    private String discordGuild;
    /**
     * Identifier of the linked Telegram channel.
     */
    private String telegramChannel;
}
