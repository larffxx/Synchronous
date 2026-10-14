package com.larffxx.synchronousweb.chat.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousweb.chat.domain.ChatAttachmentInput;
import com.larffxx.synchronousweb.chat.domain.ChatMessageInput;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Converts the existing Telegram and Discord Kafka JSON payloads into one web model.
 */
@Component
public class ChatPayloadMapper {
    private final ObjectMapper objectMapper;

    public ChatPayloadMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ChatMessageInput fromTelegram(String payload) {
        return fromPayload(payload, ChatPlatform.TELEGRAM);
    }

    public ChatMessageInput fromDiscord(String payload) {
        return fromPayload(payload, ChatPlatform.DISCORD);
    }

    private ChatMessageInput fromPayload(String payload, ChatPlatform platform) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            String conversationId = platform == ChatPlatform.TELEGRAM
                    ? firstText(node, "chatId", "guildId")
                    : firstText(node, "channelId", "guildId");
            String conversationName = platform == ChatPlatform.TELEGRAM
                    ? firstText(node, "chatName", "chatId")
                    : firstText(node, "channelName", "guildId");
            String senderName = platform == ChatPlatform.TELEGRAM
                    ? firstText(node, "telegramUserName", "senderName")
                    : firstText(node, "discordUserName", "senderName");
            String content = firstText(node, "message", "caption");
            String sourceId = firstText(node, "messageId");
            String sentAtText = firstText(node, "sentAt");
            Instant sentAt = parseInstant(sentAtText, Instant.now());
            String externalId = sourceId == null || sourceId.isBlank()
                    ? fallbackExternalId(platform, conversationId, senderName, content, sentAt)
                    : platform == ChatPlatform.TELEGRAM
                    ? safeValue(conversationId, "unknown-conversation") + ":" + sourceId
                    : sourceId;
            List<ChatAttachmentInput> attachments = attachments(node);

            return new ChatMessageInput(
                    platform,
                    externalId,
                    safeValue(conversationId, "unknown-conversation"),
                    safeValue(conversationName, safeValue(conversationId, "Unknown conversation")),
                    safeValue(senderName, "Unknown sender"),
                    safeValue(content, ""),
                    sentAt,
                    attachments
            );
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid " + platform + " chat payload", exception);
        }
    }

    private String firstText(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode value = node.get(fieldName);
            if (value != null && !value.isNull()) {
                String text = value.asText();
                if (!text.isBlank()) {
                    return text;
                }
            }
        }
        return null;
    }

    private Instant parseInstant(String value, Instant fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return Instant.parse(value);
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private List<ChatAttachmentInput> attachments(JsonNode node) {
        List<ChatAttachmentInput> attachments = new ArrayList<>();
        addAttachment(node.get("file"), attachments);
        JsonNode files = node.get("files");
        if (files != null && files.isArray()) {
            files.forEach(file -> addAttachment(file, attachments));
        }
        return attachments;
    }

    private void addAttachment(JsonNode node, List<ChatAttachmentInput> attachments) {
        if (node == null || node.isNull()) {
            return;
        }
        String sourcePath = node.isTextual() ? node.asText() : firstText(node, "path", "absolutePath");
        if (sourcePath == null || sourcePath.isBlank()) {
            return;
        }
        try {
            Path path = Path.of(sourcePath);
            String originalName = path.getFileName() == null
                    ? "image"
                    : path.getFileName().toString();
            attachments.add(new ChatAttachmentInput(sourcePath, originalName));
        } catch (RuntimeException ignored) {
            // A malformed source path becomes an unavailable attachment, not a broken message.
        }
    }

    private String fallbackExternalId(ChatPlatform platform, String conversationId,
                                      String senderName, String content, Instant sentAt) {
        String seed = String.join("|", platform.name(), safeValue(conversationId, ""),
                safeValue(senderName, ""), safeValue(content, ""), sentAt.toString());
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(seed.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            return UUID.nameUUIDFromBytes(seed.getBytes(StandardCharsets.UTF_8)).toString();
        }
    }

    private String safeValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
