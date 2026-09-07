package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for server link entities.
 * Provides lookups of Discord to Telegram channel links.
 */
@Repository
public interface ServersConnectRepository extends JpaRepository<ServersConnect, Long> {
    /**
     * Finds the server link for a Telegram channel.
     * @param telegramChannel the Telegram channel identifier
     * @return the matching server link
     */
    ServersConnect findByTelegramChannel(String telegramChannel);
    /**
     * Checks whether a Telegram channel is already linked.
     * @param telegramChatName the Telegram channel identifier
     * @return true when a link exists
     */
    boolean existsByTelegramChannel(String telegramChatName);
}
