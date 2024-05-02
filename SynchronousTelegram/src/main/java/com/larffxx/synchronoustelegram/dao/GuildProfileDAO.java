package com.larffxx.synchronoustelegram.dao;

import com.larffxx.synchronoustelegram.models.GuildProfile;
import com.larffxx.synchronoustelegram.models.UsersConnect;
import com.larffxx.synchronoustelegram.repo.GuildProfileRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class GuildProfileDAO {
    private final GuildProfileRepository guildProfileRepository;

    public GuildProfileDAO(GuildProfileRepository guildProfileRepository) {
        this.guildProfileRepository = guildProfileRepository;
    }

    public GuildProfile getByName(String name){
        return guildProfileRepository.getByName(name);
    }
}
