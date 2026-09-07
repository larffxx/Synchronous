package com.larffxx.synchronousdiscord.infrastructure.repo;

import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Users Connect Repository contract.
 */
@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    /**
     * Finds by discord name.
     * @param discordName the discord name.
     * @return the resulting users connect.
     */
    UsersConnect findByDiscordName(String discordName);
    /**
     * Finds by discord user id.
     * @param discordId the discord id.
     * @return the resulting users connect.
     */
    UsersConnect findByDiscordUserId(String discordId);
    /**
     * Finds by telegram name.
     * @param name the name.
     * @return the resulting users connect.
     */
    UsersConnect findByTelegramName(String name);
    /**
     * Updates by discord id.
     * @param discordName the discord name.
     * @param discordUserId the discord user id.
     */
    @Transactional
    @Modifying
    @Query("update UsersConnect u set u.discordName = ?1 where u.discordUserId = ?2")
    void updateByDiscordId(String discordName,String discordUserId);
    /**
     * Updates discord user id by telegram name.
     * @param discordUserId the discord user id.
     * @param telegramName the telegram name.
     */
    @Transactional
    @Modifying
    @Query("update UsersConnect u set u.discordUserId = ?1 where u.telegramName = ?2")
    void updateDiscordUserIdByTelegramName(String discordUserId, String telegramName);

    /**
     * Checks whether exists by discord user id.
     * @param discordId the discord id.
     * @return the resulting boolean.
     */
    boolean existsByDiscordUserId(String discordId);
    /**
     * Checks whether exists by discord name.
     * @param discordName the discord name.
     * @return the resulting boolean.
     */
    boolean existsByDiscordName(String discordName);
}
