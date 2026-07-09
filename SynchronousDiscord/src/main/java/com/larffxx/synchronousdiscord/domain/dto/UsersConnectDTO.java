package com.larffxx.synchronousdiscord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersConnectDTO {
    private String discordName;
    private String telegramName;
    private String discordUserId;
    private Long serversConnectId;
}
