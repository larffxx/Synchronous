package com.larffxx.synchronousweb.chat.infrastructure;

import com.larffxx.synchronousweb.chat.service.ChatMessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Reads the existing bridge topics using a web-specific consumer group.
 */
@Component
public class ChatMessageKafkaConsumer {
    private final ChatPayloadMapper payloadMapper;
    private final ChatMessageService messageService;

    public ChatMessageKafkaConsumer(ChatPayloadMapper payloadMapper, ChatMessageService messageService) {
        this.payloadMapper = payloadMapper;
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${chat.kafka.telegram-topic:Telegram}", groupId = "${chat.kafka.group-id:web-chat}")
    public void consumeTelegram(String payload) {
        messageService.ingest(payloadMapper.fromTelegram(payload));
    }

    @KafkaListener(topics = "${chat.kafka.discord-topic:Discord}", groupId = "${chat.kafka.group-id:web-chat}")
    public void consumeDiscord(String payload) {
        messageService.ingest(payloadMapper.fromDiscord(payload));
    }
}
