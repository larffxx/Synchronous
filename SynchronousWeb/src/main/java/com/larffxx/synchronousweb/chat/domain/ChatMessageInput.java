package com.larffxx.synchronousweb.chat.domain;

import java.time.Instant;
import java.util.List;

/**
 * Normalized message data produced by an external-platform payload mapper.
 */
public record ChatMessageInput(
        ChatPlatform platform,
        String externalId,
        String conversationId,
        String conversationName,
        String senderName,
        String content,
        Instant sentAt,
        List<ChatAttachmentInput> attachments
) {
}
