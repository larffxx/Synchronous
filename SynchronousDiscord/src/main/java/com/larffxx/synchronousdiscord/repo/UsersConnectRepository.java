package com.larffxx.synchronousdiscord.repo;

import com.larffxx.synchronousdiscord.model.UsersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    UsersConnect findByDiscordName(String discordName);
    UsersConnect findByDiscordId(String discordId);
    UsersConnect findByTelegramName(String name);
    boolean existsByDiscordId(String discordId);
}
