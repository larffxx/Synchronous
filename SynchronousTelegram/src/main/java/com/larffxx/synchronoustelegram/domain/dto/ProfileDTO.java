package com.larffxx.synchronoustelegram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for a guild profile.
 * Carries the profile name together with its user and server link identifiers.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    /**
     * Display name of the profile.
     */
    private String username;
    /**
     * Identifier of the linked user connection.
     */
    private Long usersConnectId;
    /**
     * Identifier of the linked server connection.
     */
    private Long serversConnectId;
}
