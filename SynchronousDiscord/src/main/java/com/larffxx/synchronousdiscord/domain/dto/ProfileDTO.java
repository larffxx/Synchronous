package com.larffxx.synchronousdiscord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private String username;
    private String discordId;
    private String discordGuild;
}
