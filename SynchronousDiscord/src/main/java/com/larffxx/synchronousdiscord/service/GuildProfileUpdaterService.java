package com.larffxx.synchronousdiscord.service;

import com.larffxx.synchronousdiscord.domain.dto.ProfileDTO;
import com.larffxx.synchronousdiscord.domain.mapper.Mapper;
import com.larffxx.synchronousdiscord.domain.mapper.ProfileMapper;
import com.larffxx.synchronousdiscord.domain.model.Profile;
import com.larffxx.synchronousdiscord.domain.context.MemberContext;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.GuildProfileRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Creates or updates stored guild profiles from member contexts.
 */
@Component
@Getter
@Setter
public class GuildProfileUpdaterService {
    /**
     * Repository for stored guild profiles.
     */
    private final GuildProfileRepository guildProfileRepository;
    /**
     * Repository for Discord to Telegram server connections.
     */
    private final ServersConnectRepository serversConnectRepository;
    /**
     * Repository for Discord to Telegram user connections.
     */
    private final UsersConnectRepository usersConnectRepository;

    /**
     * Creates a guild profile updater service.
     *
     * @param guildProfileRepository repository for guild profiles
     * @param serversConnectRepository repository for server connections
     * @param usersConnectRepository repository for user connections
     */
    public GuildProfileUpdaterService(GuildProfileRepository guildProfileRepository, ServersConnectRepository serversConnectRepository, UsersConnectRepository usersConnectRepository) {
        this.guildProfileRepository = guildProfileRepository;
        this.serversConnectRepository = serversConnectRepository;
        this.usersConnectRepository = usersConnectRepository;
    }

    /**
     * Creates or updates a stored profile for every given member.
     *
     * @param memberContext member contexts to persist
     */
    @Transactional
    public void update(List<MemberContext> memberContext){
        for(MemberContext member : memberContext){
            UsersConnect usersConnect = usersConnectRepository.findByDiscordName(member.username());
            if(usersConnect == null || !guildProfileRepository.existsByUsersConnect(usersConnect)){
                createProfile(member, usersConnect);
            }else {
                updateProfile(member, usersConnect);
            }
        }
    }

    /**
     * Creates and saves a new profile for the given member.
     *
     * @param memberContext member context to create a profile from
     * @param usersConnect the fetched user connection, may be null
     * @throws RuntimeException when the user or server connection does not exist
     */
    private void createProfile(MemberContext memberContext, UsersConnect usersConnect){
        Mapper<Profile, ProfileDTO> profileMapper = new ProfileMapper();
        ServersConnect serversConnect = serversConnectRepository.getConnectByDiscordGuild(memberContext.guildId());


        String nickname = memberContext.guildName();
        if(usersConnect != null && serversConnect != null) {
            ProfileDTO profileDTO = new ProfileDTO(nickname,
                    usersConnect.getId(),
                    serversConnect.getId());
            Profile profile = profileMapper.toEntity(profileDTO);

            guildProfileRepository.save(profile);
        }else{
            throw new RuntimeException("Users Connect or Server Not Exists");
        }

    }

    /**
     * Updates the stored profile and user connection for the given member.
     *
     * @param memberContext member context with the latest member data
     * @param usersConnect the fetched user connection to link the profile to
     */
    private void updateProfile(MemberContext memberContext, UsersConnect usersConnect){
        usersConnectRepository.updateByDiscordId(memberContext.username(), memberContext.id());
        guildProfileRepository.updateByUsersConnect(memberContext.guildName(), usersConnect);
    }
}
