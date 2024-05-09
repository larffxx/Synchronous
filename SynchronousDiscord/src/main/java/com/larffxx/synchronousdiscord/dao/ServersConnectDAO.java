package com.larffxx.synchronousdiscord.dao;

import com.larffxx.synchronousdiscord.model.ServersConnect;
import com.larffxx.synchronousdiscord.repo.ServersConnectRepository;
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

    public ServersConnect getByDiscordGuild(String discordGuild){
        return serversConnectRepository.getConnectByDiscordGuild(discordGuild);
    }

    public ServersConnect getByTelegramChat(String telegramChat){
        return serversConnectRepository.getConnectByTelegramChannel(telegramChat);
    }

    public boolean existsByDiscordGuildId(String guildId){
        return serversConnectRepository.existsByDiscordGuild(guildId);
    }

    public void saveServer(String discordGuild, String telegramGuild){
        ServersConnect serversConnect = new ServersConnect(discordGuild, telegramGuild);
        serversConnectRepository.save(serversConnect);
    }
}
