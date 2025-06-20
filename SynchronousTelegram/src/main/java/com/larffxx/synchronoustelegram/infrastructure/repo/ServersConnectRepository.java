package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServersConnectRepository extends JpaRepository<ServersConnect, Long> {
    ServersConnect getConnectByTelegramChannel(String telegramChat);
    ServersConnect findByTelegramChannel(String telegramChannel);
    ServersConnect findByDiscordGuild(String discordGuild);
    boolean existsByTelegramChannel(String telegramChatName);

    @Transactional
    @Modifying
    @Query("update ServersConnect u set u.telegramChannel = ?1 where u.telegramChannel = ?2")
    void updateByTelegramChannel(String telegramChannelId, String telegramChannel);
}
