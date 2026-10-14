package com.larffxx.synchronousweb.chat.repository;

import com.larffxx.synchronousweb.chat.domain.ChatAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Persistence boundary for stored chat images.
 */
public interface ChatAttachmentRepository extends JpaRepository<ChatAttachment, Long> {
    Optional<ChatAttachment> findByIdAndMessageId(Long id, Long messageId);
}
