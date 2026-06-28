package com.larffxx.synchronoustelegram.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandPayload{
    private String chatId;
    private String guildId;
    private String name;
    private String command;
    private List<String> options;
}
