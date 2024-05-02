package com.larffxx.synchronoustelegram.repo;

import com.larffxx.synchronoustelegram.models.UsersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersConnectRepository extends JpaRepository<UsersConnect, Long> {
    UsersConnect findByTelegramName(String telegramName);

    UsersConnect findByDiscordName(String discordName);
}
