package com.larffxx.synchronoustelegram.dao;

import com.larffxx.synchronoustelegram.model.ServersConnect;
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

    public ServersConnect getTelegramChatByDiscordGuild(String discordGuild){
        return serversConnectRepository.findByDiscordGuild(discordGuild);
    }

    public void updateTelegramChannel(String telegramChatId, String telegramChatTitle){
        serversConnectRepository.updateByTelegramChannel(telegramChatId, telegramChatTitle);
    }

    public void saveServer(String telegramGuild){
        ServersConnect serversConnect = new ServersConnect(telegramGuild);
        serversConnectRepository.save(serversConnect);
    }
}
