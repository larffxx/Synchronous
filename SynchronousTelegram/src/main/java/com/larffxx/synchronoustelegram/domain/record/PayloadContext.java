package com.larffxx.synchronoustelegram.domain.record;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class PayloadContext {
    String chatID;
    String caption;
    List<File> files;

    public PayloadContext(String chatId, String caption, List<File> files) {
        this.chatID = chatId;
        this.caption = caption;
        this.files = files;
    }
}
