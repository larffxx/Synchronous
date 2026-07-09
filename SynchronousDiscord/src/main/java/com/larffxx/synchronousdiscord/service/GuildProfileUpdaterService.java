package com.larffxx.synchronousdiscord.service;

import com.larffxx.synchronousdiscord.domain.dto.ProfileDTO;
import com.larffxx.synchronousdiscord.domain.mapper.Mapper;
import com.larffxx.synchronousdiscord.domain.mapper.ProfileMapper;
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
            if(!guildProfileRepository.existsByUsersConnect(usersConnectRepository.findByDiscordName(member.username()))){
                createProfile(member);
            }else {
                updateProfile(member);
            }
        }
    }

    private void createProfile(MemberContext memberContext){
        Mapper<Profile, ProfileDTO> profileMapper = new ProfileMapper();
        boolean usersConnectExists = usersConnectRepository.existsByDiscordName(memberContext.username());
        boolean serversConnectExists = serversConnectRepository.existsByDiscordGuild(memberContext.guildId());


        String nickname = memberContext.guildName();
        if(usersConnectExists && serversConnectExists) {
            ProfileDTO profileDTO = new ProfileDTO(nickname,
                    usersConnectRepository.findByDiscordName(memberContext.username()).getId(),
                    serversConnectRepository.getConnectByDiscordGuild(memberContext.guildId()).getId());
            Profile profile = profileMapper.toEntity(profileDTO);

            guildProfileRepository.save(profile);
        }else{
            throw new RuntimeException("Users Connect or Server Not Exists");
        }

    }

    private void updateProfile(MemberContext memberContext){
        usersConnectRepository.updateByDiscordId(memberContext.username(), memberContext.id());
        guildProfileRepository.updateByUsersConnect(memberContext.guildName(), usersConnectRepository.findByDiscordUserId(memberContext.id()));
    }
}
