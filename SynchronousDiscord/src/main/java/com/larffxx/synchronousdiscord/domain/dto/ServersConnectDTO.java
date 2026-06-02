package com.larffxx.synchronousdiscord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServersConnectDTO {
    private String discordGuild;
    private String telegramChannel;
}
