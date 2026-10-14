package com.larffxx.synchronousweb.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Metadata for an image stored by the web module.
 */
@Entity
@Table(name = "chat_attachments")
public class ChatAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage message;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;

    @Column(name = "content_type", nullable = false, length = 64)
    private String contentType;

    @Column(nullable = false)
    private long size;

    protected ChatAttachment() {
        // Required by JPA.
    }

    public ChatAttachment(ChatMessage message, StoredChatAttachment storedAttachment) {
        this.message = message;
        this.storagePath = storedAttachment.storagePath();
        this.originalName = storedAttachment.originalName();
        this.contentType = storedAttachment.contentType();
        this.size = storedAttachment.size();
    }

    public Long getId() {
        return id;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }
}
