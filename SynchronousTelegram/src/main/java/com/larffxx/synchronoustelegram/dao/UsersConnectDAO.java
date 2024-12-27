package com.larffxx.synchronoustelegram.dao;

import com.larffxx.synchronoustelegram.models.UsersConnect;
import com.larffxx.synchronoustelegram.repo.GuildProfileRepository;
import com.larffxx.synchronoustelegram.repo.UsersConnectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersConnectDAO {
    private final UsersConnectRepository usersConnectRepository;

    public UsersConnectDAO(UsersConnectRepository usersConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
    }

    public UsersConnect getByTelegramName(String name) {
        return usersConnectRepository.findByTelegramName(name);
    }

    public UsersConnect getByDiscordName(String name){
        return  usersConnectRepository.findByDiscordName(name);
    }


    public void saveData(UsersConnect usersConnect) {
        this.usersConnectRepository.save(usersConnect);
    }
}