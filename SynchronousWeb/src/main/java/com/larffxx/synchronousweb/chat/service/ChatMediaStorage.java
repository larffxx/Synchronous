package com.larffxx.synchronousweb.chat.service;

import com.larffxx.synchronousweb.chat.domain.ChatAttachmentInput;
import com.larffxx.synchronousweb.chat.domain.StoredChatAttachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Validates and copies new images into the web module's private local media directory.
 */
@Service
public class ChatMediaStorage {
    private static final long DEFAULT_MAX_IMAGE_SIZE = 10 * 1024 * 1024;

    private final Path mediaRoot;
    private final long maxImageSize;

    public ChatMediaStorage(
            @Value("${chat.media.directory:./data/chat-media}") String mediaDirectory,
            @Value("${chat.media.max-image-size:10485760}") long maxImageSize) {
        this.mediaRoot = Path.of(mediaDirectory).toAbsolutePath().normalize();
        this.maxImageSize = maxImageSize > 0 ? maxImageSize : DEFAULT_MAX_IMAGE_SIZE;
    }

    public List<StoredChatAttachment> store(List<ChatAttachmentInput> inputs) {
        List<StoredChatAttachment> stored = new ArrayList<>();
        if (inputs == null || inputs.isEmpty()) {
            return stored;
        }
        try {
            Files.createDirectories(mediaRoot);
        } catch (IOException exception) {
            return stored;
        }

        for (ChatAttachmentInput input : inputs) {
            storeOne(input).ifPresent(stored::add);
        }
        return stored;
    }

    public Resource load(String storagePath) {
        Path candidate = mediaRoot.resolve(storagePath).normalize();
        if (!candidate.startsWith(mediaRoot) || !Files.isRegularFile(candidate)) {
            return null;
        }
        return new FileSystemResource(candidate);
    }

    private java.util.Optional<StoredChatAttachment> storeOne(ChatAttachmentInput input) {
        try {
            Path source = Path.of(input.sourcePath()).toAbsolutePath().normalize();
            if (!Files.isRegularFile(source)) {
                return java.util.Optional.empty();
            }
            long size = Files.size(source);
            if (size <= 0 || size > maxImageSize) {
                return java.util.Optional.empty();
            }

            String contentType = detectContentType(source);
            if (!isAllowedImageType(contentType)) {
                return java.util.Optional.empty();
            }

            String extension = extensionFor(contentType, source);
            String storageName = UUID.randomUUID() + extension;
            Path destination = mediaRoot.resolve(storageName).normalize();
            if (!destination.startsWith(mediaRoot)) {
                return java.util.Optional.empty();
            }
            Files.copy(source, destination);
            String originalName = Path.of(input.originalName()).getFileName().toString();
            return java.util.Optional.of(new StoredChatAttachment(
                    storageName, originalName, contentType, size));
        } catch (IOException | RuntimeException exception) {
            return java.util.Optional.empty();
        }
    }

    private String detectContentType(Path source) throws IOException {
        String detected = Files.probeContentType(source);
        if (detected != null) {
            return detected.toLowerCase(Locale.ROOT);
        }
        String name = source.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".gif")) return "image/gif";
        if (name.endsWith(".webp")) return "image/webp";
        return "application/octet-stream";
    }

    private boolean isAllowedImageType(String contentType) {
        return switch (contentType) {
            case "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp" -> true;
            default -> false;
        };
    }

    private String extensionFor(String contentType, Path source) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> source.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jpeg")
                    ? ".jpeg" : ".jpg";
        };
    }
}
