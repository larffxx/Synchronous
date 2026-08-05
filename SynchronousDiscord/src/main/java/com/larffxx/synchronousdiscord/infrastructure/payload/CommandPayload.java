package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandPayload {
    private String guildId;
    private String telegramChatId;
    private String discordUserName;
    private String commandName;
    private List<String> options;
    private List<String> response;
}
