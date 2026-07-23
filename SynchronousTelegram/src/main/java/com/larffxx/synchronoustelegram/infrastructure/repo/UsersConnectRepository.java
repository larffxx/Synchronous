package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    UsersConnect findByTelegramName(String telegramName);

    UsersConnect findByDiscordName(String discordName);

    boolean existsByTelegramName(String telegramName);
}
