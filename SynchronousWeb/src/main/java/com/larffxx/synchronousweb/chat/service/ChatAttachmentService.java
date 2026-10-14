package com.larffxx.synchronousweb.chat.service;

import com.larffxx.synchronousweb.chat.domain.ChatAttachment;
import com.larffxx.synchronousweb.chat.repository.ChatAttachmentRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Resolves persisted attachment metadata and validates its local file.
 */
@Service
public class ChatAttachmentService {
    private final ChatAttachmentRepository repository;
    private final ChatMediaStorage mediaStorage;

    public ChatAttachmentService(ChatAttachmentRepository repository, ChatMediaStorage mediaStorage) {
        this.repository = repository;
        this.mediaStorage = mediaStorage;
    }

    @Transactional(readOnly = true)
    public Optional<ChatAttachmentDownload> find(Long messageId, Long attachmentId) {
        return repository.findByIdAndMessageId(attachmentId, messageId)
                .map(attachment -> new ChatAttachmentDownload(attachment, mediaStorage.load(attachment.getStoragePath())))
                .filter(download -> download.resource() != null);
    }

    public record ChatAttachmentDownload(ChatAttachment attachment, Resource resource) {
    }
}
