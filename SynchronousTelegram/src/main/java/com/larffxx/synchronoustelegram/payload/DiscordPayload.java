package com.larffxx.synchronoustelegram.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DiscordPayload {
    private Long guildID;
    private String author;
    private String message;
    private String command;
    private List<File> files;
}
