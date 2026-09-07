package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Command Payload class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandPayload {
    /**
     * The guild id.
     */
    private String guildId;
    /**
     * The telegram chat id.
     */
    private String telegramChatId;
    /**
     * The discord user name.
     */
    private String discordUserName;
    /**
     * The command name.
     */
    private String commandName;
    /**
     * The options.
     */
    private List<String> options;
    /**
     * The response.
     */
    private List<String> response;
}
