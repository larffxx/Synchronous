package com.larffxx.synchronousweb.chat.domain;

/**
 * Validated attachment copied into the web module's local media directory.
 */
public record StoredChatAttachment(
        String storagePath,
        String originalName,
        String contentType,
        long size
) {
}
