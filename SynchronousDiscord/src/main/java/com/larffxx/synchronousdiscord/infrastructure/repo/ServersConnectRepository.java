package com.larffxx.synchronousdiscord.infrastructure.repo;

import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServersConnectRepository extends JpaRepository<ServersConnect, Long> {
    ServersConnect getConnectByDiscordGuild(String discordGuild);

    ServersConnect getConnectByTelegramChannel(String telegramChannel);

    boolean existsByDiscordGuild(String discordGuild);
}
