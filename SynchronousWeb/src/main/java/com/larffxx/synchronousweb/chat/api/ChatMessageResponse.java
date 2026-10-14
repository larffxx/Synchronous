package com.larffxx.synchronousweb.chat.api;

import com.larffxx.synchronousweb.chat.domain.ChatMessage;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;

import java.time.Instant;
import java.util.List;

/**
 * Browser-facing representation of a normalized chat message.
 */
public record ChatMessageResponse(
        Long id,
        String externalId,
        ChatPlatform platform,
        String conversationId,
        String conversationName,
        String senderName,
        String content,
        Instant sentAt,
        int attachmentCount,
        List<ChatAttachmentResponse> attachments
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getExternalId(),
                message.getPlatform(),
                message.getConversationId(),
                message.getConversationName(),
                message.getSenderName(),
                message.getContent(),
                message.getSentAt(),
                message.getAttachmentCount(),
                message.getAttachments().stream().map(ChatAttachmentResponse::from).toList()
        );
    }
}
