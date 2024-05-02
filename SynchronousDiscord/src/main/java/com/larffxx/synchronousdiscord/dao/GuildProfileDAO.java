package com.larffxx.synchronousdiscord.dao;

import com.larffxx.synchronousdiscord.model.GuildProfile;
import com.larffxx.synchronousdiscord.model.Profile;
import com.larffxx.synchronousdiscord.model.ServersConnect;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.repo.GuildProfileRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Transient;
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

    public void setGuildProfile(String name, UsersConnect usersConnect, ServersConnect serversConnect, Profile profile){
        GuildProfile guildProfile = new GuildProfile(name, usersConnect, serversConnect, profile);
        guildProfileRepository.save(guildProfile);
    }

    public boolean existsByUserId(UsersConnect usersConnect){
        return guildProfileRepository.existsByUsersConnect(usersConnect);
    }

    public GuildProfile getGuildProfileByUsersConnect(UsersConnect usersConnect){
        return guildProfileRepository.getGuildProfileByUsersConnect(usersConnect);
    }

    public void updateGuildProfile(String name, UsersConnect usersConnect){
        guildProfileRepository.updateByUsersConnect(name, usersConnect);
    }

}
