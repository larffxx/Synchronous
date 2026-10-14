package com.larffxx.synchronousweb.chat.api;

import com.larffxx.synchronousweb.chat.domain.ChatAttachment;

/**
 * Browser-facing image metadata.
 */
public record ChatAttachmentResponse(
        Long id,
        String originalName,
        String contentType,
        long size,
        String url
) {
    public static ChatAttachmentResponse from(ChatAttachment attachment) {
        return new ChatAttachmentResponse(
                attachment.getId(),
                attachment.getOriginalName(),
                attachment.getContentType(),
                attachment.getSize(),
                "/api/messages/" + attachment.getMessage().getId()
                        + "/attachments/" + attachment.getId()
        );
    }
}
