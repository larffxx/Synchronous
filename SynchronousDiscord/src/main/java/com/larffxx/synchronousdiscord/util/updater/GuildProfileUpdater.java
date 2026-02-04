package com.larffxx.synchronousdiscord.util.updater;

import com.larffxx.synchronousdiscord.domain.model.Profile;
import com.larffxx.synchronousdiscord.infrastructure.repo.GuildProfileRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class GuildProfileUpdater {
    private final GuildProfileRepository guildProfileRepository;
    private final ServersConnectRepository serversConnectRepository;
    private final UsersConnectRepository usersConnectRepository;

    public GuildProfileUpdater(GuildProfileRepository guildProfileRepository, ServersConnectRepository serversConnectRepository, UsersConnectRepository usersConnectRepository) {
        this.guildProfileRepository = guildProfileRepository;
        this.serversConnectRepository = serversConnectRepository;
        this.usersConnectRepository = usersConnectRepository;
    }

    public void update(List<Member> members, Guild guild){
        for(Member member : members){
            if(!guildProfileRepository.existsByUsersConnect(usersConnectRepository.findByDiscordId(member.getId()))) {
                createProfile(member, guild);
            }else {
                updateProfile(member);
            }
        }
    }

    private void createProfile(Member member, Guild guild){
        String nickname = member.getNickname();
        Profile profile = new Profile(nickname, usersConnectRepository.findByDiscordName(nickname), serversConnectRepository.getConnectByDiscordGuild(guild.getId()));

        guildProfileRepository.save(profile);
    }

    private void updateProfile(Member member){
        usersConnectRepository.updateByDiscordId(member.getUser().getName(), member.getUser().getId());
        guildProfileRepository.updateByUsersConnect(member.getNickname(), usersConnectRepository.findByDiscordId(member.getId()));
    }
}
