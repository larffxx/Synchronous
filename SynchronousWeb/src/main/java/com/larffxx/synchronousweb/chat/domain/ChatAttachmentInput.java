package com.larffxx.synchronousweb.chat.domain;

/**
 * Source attachment reference received in a Telegram or Discord Kafka payload.
 */
public record ChatAttachmentInput(
        String sourcePath,
        String originalName
) {
}
