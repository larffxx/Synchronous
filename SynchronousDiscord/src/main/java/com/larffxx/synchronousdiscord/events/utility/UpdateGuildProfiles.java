package com.larffxx.synchronousdiscord.events.utility;

import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class UpdateGuildProfiles {
    private final ProfileDAO profileDAO;
    private final ServersConnectDAO serversConnectDAO;
    private final GuildProfileDAO guildProfileDAO;
    private final UsersConnectDAO usersConnectDAO;

    public UpdateGuildProfiles(ProfileDAO profileDAO, ServersConnectDAO serversConnectDAO, GuildProfileDAO guildProfileDAO, UsersConnectDAO usersConnectDAO) {
        this.profileDAO = profileDAO;
        this.serversConnectDAO = serversConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
        this.usersConnectDAO = usersConnectDAO;
    }

    public void update(List<Member> members, Guild guild){
        for(Member member : members){
            if(!guildProfileDAO.existsByUserConnect(usersConnectDAO.getByDiscordId(member.getId()))) {
                createProfile(member, guild);
            }else {
                updateProfile(member,guild);
            }
        }
    }

    private void createProfile(Member member, Guild guild){
        guildProfileDAO.setGuildProfile(member.getNickname(),
                usersConnectDAO.getByDiscordName(member.getUser().getName()),
                serversConnectDAO.getByDiscordGuild(guild.getId()),
                profileDAO.getProfile(member.getUser().getName()));
    }

    private void updateProfile(Member member, Guild guild){
        usersConnectDAO.updateByDiscordId(member.getUser().getName(), member.getUser().getId());
        guildProfileDAO.updateGuildProfile(member.getNickname(), usersConnectDAO.getByDiscordId(member.getId()));
    }
}
