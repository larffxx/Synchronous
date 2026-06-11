package com.larffxx.synchronoustelegram.domain.context;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
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
        private Long telegramChatId;
        private Long guildId;
        private String author;
        private String message;
        private MessageType messageType;
        private List<File> fileList;

        public MessageContext(Long telegramChatId, Long guildId, String author,  String message, MessageType messageType) {
            this.telegramChatId = telegramChatId;
            this.guildId = guildId;
            this.author = author;
            this.message = message;
            this.messageType = messageType;
        }
}
