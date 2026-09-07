package com.larffxx.synchronousdiscord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Servers Connect DTO class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServersConnectDTO {
    /**
     * The discord guild.
     */
    private String discordGuild;
    /**
     * The telegram channel.
     */
    private String telegramChannel;
}
