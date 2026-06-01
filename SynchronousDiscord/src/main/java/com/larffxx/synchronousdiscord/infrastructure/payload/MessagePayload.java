package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MessagePayload {
    private Long guildId;
    private String authorName;
    private String message;
    private List<File> files;
}
