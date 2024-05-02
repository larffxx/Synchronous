package com.larffxx.synchronoustelegram.dao;

import com.larffxx.synchronoustelegram.models.ServersConnect;
import com.larffxx.synchronoustelegram.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class ServersConnectDAO {
    private final ServersConnectRepository serversConnectRepository;
    private final UsersConnectDAO usersConnectDAO;

    public ServersConnectDAO(ServersConnectRepository serversConnectRepository, UsersConnectDAO usersConnectDAO) {
        this.serversConnectRepository = serversConnectRepository;
        this.usersConnectDAO = usersConnectDAO;
    }

    public ServersConnect getByTelegramChat(String telegramChat){
        return serversConnectRepository.getConnectByTelegramChannel(telegramChat);
    }

    public boolean existsByTelegramChatName(String telegramChatName){
        return serversConnectRepository.existsByTelegramChannel(telegramChatName);
    }

    public ServersConnect getChatByDiscordGuild(String telegramChat){
        return serversConnectRepository.findByDiscordGuild(telegramChat);
    }

    public void updateTelegramChannel(String telegramChat, String telegramChannel){
        serversConnectRepository.updateByTelegramChannel(telegramChat, telegramChannel);
    }

    public void saveServer(String telegramGuild){
        ServersConnect serversConnect = new ServersConnect(telegramGuild);
        serversConnectRepository.save(serversConnect);
    }
}
