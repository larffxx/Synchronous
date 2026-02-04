package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommandPayload {
    private String guildId;
    private String authorName;
    private String command;
    private List<String> options;
}
