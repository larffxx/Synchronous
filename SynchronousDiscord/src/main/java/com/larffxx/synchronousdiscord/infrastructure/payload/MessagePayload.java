package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
    private Long guildId;
    private Long telegramChatId;
    private String authorName;
    private String message;
    private List<File> files;
    private MessageType messageType;

    public MessagePayload(Long guildId, Long telegramChatId, String authorName, String message, List<File> files) {
        this.guildId = guildId;
        this.telegramChatId = telegramChatId;
        this.authorName = authorName;
        this.message = message;
        this.files = files;
        this.messageType = MessageType.PHOTO_MESSAGE;
    }
}
