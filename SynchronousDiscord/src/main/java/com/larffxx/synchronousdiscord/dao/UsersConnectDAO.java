package com.larffxx.synchronousdiscord.dao;

import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.repo.UsersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class UsersConnectDAO {
    private final UsersConnectRepository usersConnectRepository;

    public UsersConnectDAO(UsersConnectRepository usersConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
    }

    public UsersConnect getByDiscordName(String name){
        return usersConnectRepository.findByDiscordName(name);
    }

    public UsersConnect getByDiscordId(String id){
        return usersConnectRepository.findByDiscordId(id);
    }
    public boolean existsByDiscordId(String discordGuildId){
        return usersConnectRepository.existsByDiscordId(discordGuildId);
    }

    public UsersConnect getByTelegramName(String name){
        return usersConnectRepository.findByTelegramName(name);
    }

    public void saveData(UsersConnect usersConnect){
        usersConnectRepository.save(usersConnect);
    }
}
