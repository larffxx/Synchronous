package com.larffxx.synchronousdiscord.repo;

import com.larffxx.synchronousdiscord.model.UsersConnect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    UsersConnect findByDiscordName(String discordName);
    UsersConnect findByDiscordId(String discordId);
    UsersConnect findByTelegramName(String name);
    @Transactional
    @Modifying
    @Query("update UsersConnect u set u.discordName = ?1 where u.discordId = ?2")
    void updateByDiscordId(String discordName,String discordId);

    boolean existsByDiscordId(String discordId);
}
