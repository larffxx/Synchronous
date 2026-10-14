package com.larffxx.synchronousweb.chat.service;

import com.larffxx.synchronousweb.chat.domain.ChatMessage;
import com.larffxx.synchronousweb.chat.domain.ChatMessageInput;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import com.larffxx.synchronousweb.chat.repository.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {
    @Mock
    private ChatMessageRepository repository;
    @Mock
    private ChatMessageStream stream;
    @Mock
    private ChatMediaStorage mediaStorage;

    @Test
    void storesAndPublishesNewMessage() {
        ChatMessageService service = new ChatMessageService(repository, stream, mediaStorage);
        ChatMessageInput input = new ChatMessageInput(
                ChatPlatform.DISCORD, "discord-1", "channel-1", "general",
                "alice", "Hello", Instant.parse("2026-10-14T10:15:30Z"), List.of());
        ChatMessage saved = new ChatMessage(
                input.platform(), input.externalId(), input.conversationId(), input.conversationName(),
                input.senderName(), input.content(), input.sentAt(), Instant.now(), 0);

        when(repository.findByPlatformAndExternalId(ChatPlatform.DISCORD, "discord-1"))
                .thenReturn(Optional.empty());
        when(mediaStorage.store(List.of())).thenReturn(List.of());
        when(repository.saveAndFlush(any(ChatMessage.class))).thenReturn(saved);

        Optional<?> result = service.ingest(input);

        assertThat(result).isPresent();
        verify(stream).publish(any());
        verify(repository).saveAndFlush(any(ChatMessage.class));
    }

    @Test
    void ignoresDuplicateMessage() {
        ChatMessageService service = new ChatMessageService(repository, stream, mediaStorage);
        ChatMessage existing = new ChatMessage(
                ChatPlatform.TELEGRAM, "telegram-1", "42", "Team", "alice", "Hello",
                Instant.now(), Instant.now(), 0);
        when(repository.findByPlatformAndExternalId(ChatPlatform.TELEGRAM, "telegram-1"))
                .thenReturn(Optional.of(existing));

        Optional<?> result = service.ingest(new ChatMessageInput(
                ChatPlatform.TELEGRAM, "telegram-1", "42", "Team", "alice", "Hello",
                Instant.now(), List.of()));

        assertThat(result).isEmpty();
        verify(repository, never()).saveAndFlush(any(ChatMessage.class));
        verify(stream, never()).publish(any());
    }
}
