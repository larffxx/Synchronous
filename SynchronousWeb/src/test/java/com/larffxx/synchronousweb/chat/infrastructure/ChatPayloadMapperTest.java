package com.larffxx.synchronousweb.chat.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.larffxx.synchronousweb.chat.domain.ChatMessageInput;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ChatPayloadMapperTest {
    private final ChatPayloadMapper mapper = new ChatPayloadMapper(
            new ObjectMapper().registerModule(new JavaTimeModule()));

    @Test
    void mapsTelegramPayloadToUnifiedMessage() {
        ChatMessageInput result = mapper.fromTelegram("""
                {
                  "chatId": 42,
                  "chatName": "Team Telegram",
                  "telegramUserName": "alice",
                  "message": "Hello from Telegram",
                  "messageId": "telegram-7",
                  "sentAt": "2026-10-14T10:15:30Z",
                  "files": ["/tmp/photo.jpg"]
                }
                """);

        assertThat(result.platform()).isEqualTo(ChatPlatform.TELEGRAM);
        assertThat(result.externalId()).isEqualTo("42:telegram-7");
        assertThat(result.conversationId()).isEqualTo("42");
        assertThat(result.conversationName()).isEqualTo("Team Telegram");
        assertThat(result.senderName()).isEqualTo("alice");
        assertThat(result.content()).isEqualTo("Hello from Telegram");
        assertThat(result.sentAt()).isEqualTo(Instant.parse("2026-10-14T10:15:30Z"));
        assertThat(result.attachments()).hasSize(1);
    }

    @Test
    void usesDiscordChannelAsConversationAndCreatesFallbackId() {
        ChatMessageInput result = mapper.fromDiscord("""
                {
                  "guildId": 12,
                  "channelId": "channel-5",
                  "channelName": "general",
                  "discordUserName": "bob",
                  "message": "Hello from Discord",
                  "sentAt": "2026-10-14T10:15:30Z"
                }
                """);

        assertThat(result.platform()).isEqualTo(ChatPlatform.DISCORD);
        assertThat(result.externalId()).isNotBlank();
        assertThat(result.conversationId()).isEqualTo("channel-5");
        assertThat(result.conversationName()).isEqualTo("general");
        assertThat(result.senderName()).isEqualTo("bob");
    }
}
