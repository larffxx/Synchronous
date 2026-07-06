package com.larffxx.synchronousdiscord.service;

import com.larffxx.synchronousdiscord.domain.model.Profile;
import com.larffxx.synchronousdiscord.domain.context.MemberContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.GuildProfileRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class GuildProfileUpdaterService {
    private final GuildProfileRepository guildProfileRepository;
    private final ServersConnectRepository serversConnectRepository;
    private final UsersConnectRepository usersConnectRepository;

    public GuildProfileUpdaterService(GuildProfileRepository guildProfileRepository, ServersConnectRepository serversConnectRepository, UsersConnectRepository usersConnectRepository) {
        this.guildProfileRepository = guildProfileRepository;
        this.serversConnectRepository = serversConnectRepository;
        this.usersConnectRepository = usersConnectRepository;
    }

    public void update(List<MemberContext> memberContext){
        for(MemberContext member : memberContext){
            if(!guildProfileRepository.existsByUsersConnect(usersConnectRepository.findByDiscordName(member.effectiveName()))){
                createProfile(member);
            }else {
                updateProfile(member);
            }
        }
    }

    private void createProfile(MemberContext memberContext){
        String nickname = memberContext.guildName();
        Profile profile = new Profile(nickname, usersConnectRepository.findByDiscordName(memberContext.effectiveName()), serversConnectRepository.getConnectByDiscordGuild(memberContext.guildId()));

        guildProfileRepository.save(profile);
    }

    private void updateProfile(MemberContext memberContext){
        usersConnectRepository.updateByDiscordId(memberContext.effectiveName(), memberContext.id());
        guildProfileRepository.updateByUsersConnect(memberContext.guildName(), usersConnectRepository.findByDiscordId(memberContext.id()));
    }
}
