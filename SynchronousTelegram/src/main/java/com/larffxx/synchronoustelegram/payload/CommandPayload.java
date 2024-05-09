package com.larffxx.synchronoustelegram.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommandPayload{
    private Long chatId;
    private String name, command;
    private List<String> options;
}
