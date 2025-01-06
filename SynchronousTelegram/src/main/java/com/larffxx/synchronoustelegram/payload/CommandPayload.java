package com.larffxx.synchronoustelegram.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommandPayload{
    private String chatId;
    private String name;
    private String command;
    private List<String> options;
}
