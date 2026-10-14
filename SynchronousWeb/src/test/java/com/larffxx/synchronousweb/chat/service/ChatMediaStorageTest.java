package com.larffxx.synchronousweb.chat.service;

import com.larffxx.synchronousweb.chat.domain.ChatAttachmentInput;
import com.larffxx.synchronousweb.chat.domain.StoredChatAttachment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMediaStorageTest {
    @TempDir
    Path tempDirectory;

    @Test
    void copiesAllowedImageAndKeepsItInsideMediaRoot() throws Exception {
        Path source = tempDirectory.resolve("photo.jpg");
        Files.write(source, new byte[]{1, 2, 3});
        ChatMediaStorage storage = new ChatMediaStorage(
                tempDirectory.resolve("media").toString(), 1024);

        List<StoredChatAttachment> result = storage.store(List.of(
                new ChatAttachmentInput(source.toString(), "photo.jpg")));

        assertThat(result).hasSize(1);
        Resource stored = storage.load(result.get(0).storagePath());
        assertThat(stored).isNotNull();
        assertThat(stored.contentLength()).isEqualTo(3);
    }

    @Test
    void skipsUnsupportedAndOversizedFiles() throws Exception {
        Path text = tempDirectory.resolve("notes.txt");
        Path largeImage = tempDirectory.resolve("large.png");
        Files.writeString(text, "not an image");
        Files.write(largeImage, new byte[]{1, 2, 3, 4});
        ChatMediaStorage storage = new ChatMediaStorage(
                tempDirectory.resolve("media").toString(), 3);

        List<StoredChatAttachment> result = storage.store(List.of(
                new ChatAttachmentInput(text.toString(), "notes.txt"),
                new ChatAttachmentInput(largeImage.toString(), "large.png")));

        assertThat(result).isEmpty();
    }
}
