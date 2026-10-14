package com.larffxx.synchronousweb.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Normalized, read-only message history shown by the web chat.
 */
@Entity
@Table(name = "chat_messages", uniqueConstraints = {
        @UniqueConstraint(name = "uk_chat_messages_platform_external",
                columnNames = {"platform", "external_id"})
})
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ChatPlatform platform;

    @Column(name = "external_id", nullable = false, length = 200)
    private String externalId;

    @Column(name = "conversation_id", nullable = false, length = 100)
    private String conversationId;

    @Column(name = "conversation_name", nullable = false, length = 255)
    private String conversationName;

    @Column(name = "sender_name", nullable = false, length = 255)
    private String senderName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "attachment_count", nullable = false)
    private int attachmentCount;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private final List<ChatAttachment> attachments = new ArrayList<>();

    protected ChatMessage() {
        // Required by JPA.
    }

    public ChatMessage(ChatPlatform platform, String externalId, String conversationId,
                       String conversationName, String senderName, String content,
                       Instant sentAt, Instant receivedAt, int attachmentCount) {
        this.platform = platform;
        this.externalId = externalId;
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
        this.receivedAt = receivedAt;
        this.attachmentCount = attachmentCount;
    }

    public void addAttachment(ChatAttachment attachment) {
        attachments.add(attachment);
    }

    public Long getId() {
        return id;
    }

    public ChatPlatform getPlatform() {
        return platform;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public List<ChatAttachment> getAttachments() {
        return attachments;
    }
}
