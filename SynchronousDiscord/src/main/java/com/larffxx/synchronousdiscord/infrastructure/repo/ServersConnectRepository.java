package com.larffxx.synchronousdiscord.infrastructure.repo;

import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Servers Connect Repository contract.
 */
@Repository
public interface ServersConnectRepository extends JpaRepository<ServersConnect, Long> {
    /**
     * Returns connect by discord guild.
     * @param discordGuild the discord guild.
     * @return the resulting servers connect.
     */
    ServersConnect getConnectByDiscordGuild(String discordGuild);

    /**
     * Returns connect by telegram channel.
     * @param telegramChannel the telegram channel.
     * @return the resulting servers connect.
     */
    ServersConnect getConnectByTelegramChannel(String telegramChannel);

    /**
     * Checks whether exists by discord guild.
     * @param discordGuild the discord guild.
     * @return the resulting boolean.
     */
    boolean existsByDiscordGuild(String discordGuild);
}
