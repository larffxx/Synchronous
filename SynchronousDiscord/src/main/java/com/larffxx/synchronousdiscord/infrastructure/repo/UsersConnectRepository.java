package com.larffxx.synchronousdiscord.infrastructure.repo;

import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    UsersConnect findByDiscordName(String discordName);
    UsersConnect findByDiscordUserId(String discordId);
    UsersConnect findByTelegramName(String name);
    @Transactional
    @Modifying
    @Query("update UsersConnect u set u.discordName = ?1 where u.discordUserId = ?2")
    void updateByDiscordId(String discordName,String discordUserId);
    @Transactional
    @Modifying
    @Query("update UsersConnect u set u.discordUserId = ?1 where u.telegramName = ?2")
    void updateDiscordUserIdByTelegramName(String discordUserId, String telegramName);

    boolean existsByDiscordUserId(String discordId);
    boolean existsByDiscordName(String discordName);
}
