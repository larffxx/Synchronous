package com.larffxx.synchronoustelegram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for a Discord to Telegram user link.
 * Carries the server link identifier together with the Discord and Telegram user details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersConnectDTO {
    /**
     * Identifier of the server link this user link belongs to.
     */
    private Long serversConnectId;
    /**
     * Discord name of the linked user.
     */
    private String discordName;
    /**
     * Telegram name of the linked user.
     */
    private String telegramName;
    /**
     * Discord user identifier of the linked user.
     */
    private String discordUserId;

    /**
     * Creates a user link transfer object without the Discord user id.
     * @param serversConnectId the server link identifier
     * @param discordName the Discord name
     * @param telegramName the Telegram name
     */
    public UsersConnectDTO(Long serversConnectId, String discordName, String telegramName) {
        this.serversConnectId = serversConnectId;
        this.discordName = discordName;
        this.telegramName = telegramName;
    }
}
