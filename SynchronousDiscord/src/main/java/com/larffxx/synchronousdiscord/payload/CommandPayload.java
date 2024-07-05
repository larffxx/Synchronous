package com.larffxx.synchronousdiscord.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommandPayload {
    private String guildId;
    private String name;
    private String command;
    private List<String> options;
}
