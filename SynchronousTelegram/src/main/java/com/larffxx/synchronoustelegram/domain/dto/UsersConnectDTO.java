package com.larffxx.synchronoustelegram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersConnectDTO {
    private Long serversConnectId;
    private String discordName;
    private String telegramName;
    private String discordUserId;

    public UsersConnectDTO(Long serversConnectId, String discordName, String telegramName) {
        this.serversConnectId = serversConnectId;
        this.discordName = discordName;
        this.telegramName = telegramName;
    }
}
