package com.larffxx.synchronousdiscord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Profile DTO class.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    /**
     * The username.
     */
    private String username;
    /**
     * The users connect id.
     */
    private Long usersConnectId;
    /**
     * The servers connect id.
     */
    private Long serversConnectId;
}
