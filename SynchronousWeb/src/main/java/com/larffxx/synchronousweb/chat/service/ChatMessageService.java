package com.larffxx.synchronousweb.chat.service;

import com.larffxx.synchronousweb.chat.api.ChatMessageResponse;
import com.larffxx.synchronousweb.chat.domain.ChatAttachment;
import com.larffxx.synchronousweb.chat.domain.ChatMessage;
import com.larffxx.synchronousweb.chat.domain.ChatMessageInput;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import com.larffxx.synchronousweb.chat.repository.ChatMessageRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Stores normalized messages and serves the web chat query model.
 */
@Service
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatMessageStream stream;
    private final ChatMediaStorage mediaStorage;

    public ChatMessageService(ChatMessageRepository repository, ChatMessageStream stream,
                              ChatMediaStorage mediaStorage) {
        this.repository = repository;
        this.stream = stream;
        this.mediaStorage = mediaStorage;
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> findMessages(ChatPlatform platform, String conversationId,
                                                   Instant from, Instant to, Pageable pageable) {
        Specification<ChatMessage> specification = (root, query, builder) -> builder.conjunction();
        if (platform != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("platform"), platform));
        }
        if (conversationId != null && !conversationId.isBlank()) {
            String pattern = "%" + conversationId.trim().toLowerCase() + "%";
            specification = specification.and((root, query, builder) -> builder.or(
                    builder.like(builder.lower(root.get("conversationId")), pattern),
                    builder.like(builder.lower(root.get("conversationName")), pattern)
            ));
        }
        if (from != null) {
            specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.<Instant>get("sentAt"), from));
        }
        if (to != null) {
            specification = specification.and((root, query, builder) ->
                    builder.lessThan(root.<Instant>get("sentAt"), to));
        }
        return repository.findAll(specification, pageable).map(ChatMessageResponse::from);
    }

    @Transactional
    public Optional<ChatMessageResponse> ingest(ChatMessageInput input) {
        Optional<ChatMessage> existing = repository.findByPlatformAndExternalId(
                input.platform(), input.externalId());
        if (existing.isPresent()) {
            return Optional.empty();
        }

        var storedAttachments = mediaStorage.store(input.attachments());
        ChatMessage message = new ChatMessage(
                input.platform(),
                input.externalId(),
                input.conversationId(),
                input.conversationName(),
                input.senderName(),
                input.content(),
                input.sentAt(),
                Instant.now(),
                storedAttachments.size()
        );
        storedAttachments.forEach(stored -> message.addAttachment(new ChatAttachment(message, stored)));

        try {
            ChatMessage saved = repository.saveAndFlush(message);
            ChatMessageResponse response = ChatMessageResponse.from(saved);
            stream.publish(response);
            return Optional.of(response);
        } catch (DataIntegrityViolationException duplicate) {
            // Another Kafka delivery won the unique-key race. It is safe to acknowledge this one.
            return Optional.empty();
        }
    }
}
