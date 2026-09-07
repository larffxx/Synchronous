package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for user link entities.
 * Provides lookups of linked Discord and Telegram users.
 */
@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    /**
     * Finds a user link by Telegram name.
     * @param telegramName the Telegram name to search for
     * @return the matching user link
     */
    UsersConnect findByTelegramName(String telegramName);

    /**
     * Finds a user link by Discord name.
     * @param discordName the Discord name to search for
     * @return the matching user link
     */
    UsersConnect findByDiscordName(String discordName);

    /**
     * Checks whether a Telegram name is already registered.
     * @param telegramName the Telegram name to check
     * @return true when a link exists
     */
    boolean existsByTelegramName(String telegramName);
}
