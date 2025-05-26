package com.larffxx.synchronoustelegram.infrastructure.payload;

import com.larffxx.synchronoustelegram.infrastructure.payload.utility.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiscordPayload {
    private Long guildID;
    private String author;
    private String message;
    private Command command;
    private List<File> files;

    public DiscordPayload(Long guildID, String author, String message, List<File> files) {
        this.guildID = guildID;
        this.author = author;
        this.message = message;
        this.files = files;
    }

    public DiscordPayload(Long guildID, String author, Command command) {
        this.guildID = guildID;
        this.author = author;
        this.command = command;
    }
}
