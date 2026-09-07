package com.larffxx.synchronousdiscord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Users Connect DTO class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersConnectDTO {
    /**
     * The discord name.
     */
    private String discordName;
    /**
     * The telegram name.
     */
    private String telegramName;
    /**
     * The discord user id.
     */
    private String discordUserId;
    /**
     * The servers connect id.
     */
    private Long serversConnectId;
}
