package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServersConnectRepository extends JpaRepository<ServersConnect, Long> {
    ServersConnect findByTelegramChannel(String telegramChannel);
    boolean existsByTelegramChannel(String telegramChatName);
}
