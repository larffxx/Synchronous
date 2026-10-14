package com.larffxx.synchronousweb.chat.repository;

import com.larffxx.synchronousweb.chat.domain.ChatMessage;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Persistence boundary for normalized chat messages.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,
        JpaSpecificationExecutor<ChatMessage> {
    Optional<ChatMessage> findByPlatformAndExternalId(ChatPlatform platform, String externalId);
}
