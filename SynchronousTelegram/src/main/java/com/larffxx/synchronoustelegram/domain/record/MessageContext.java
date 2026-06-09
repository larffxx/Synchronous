package com.larffxx.synchronoustelegram.domain.record;

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
public class MessageContext {
        private String telegramChatId;
        private String guildId;
        private String message;
        private String messageType;
        private List<File> fileList;

        //TODO: context in mapper
        public MessageContext(String telegramChatId, String guildId, String message, String messageType) {
            this.telegramChatId = telegramChatId;
            this.guildId = guildId;
            this.message = message;
            this.messageType = messageType;
        }
}
