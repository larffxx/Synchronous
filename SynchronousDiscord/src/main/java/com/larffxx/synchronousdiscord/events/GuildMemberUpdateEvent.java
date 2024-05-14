package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GuildMemberUpdateEvent extends Event<net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent>{
    private final ProfileDAO profileDAO;
    private final ServersConnectDAO serversConnectDAO;
    private final GuildProfileDAO guildProfileDAO;
    private final UsersConnectDAO usersConnectDAO;
    public GuildMemberUpdateEvent(EventReceiver eventReceiver, UsersConnectDAO usersConnectDAO, GuildProfileDAO guildProfileDAO, ServersConnectDAO serversConnectDAO, ProfileDAO profileDAO) {
        super(eventReceiver);
        this.usersConnectDAO = usersConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
        this.serversConnectDAO = serversConnectDAO;
        this.profileDAO = profileDAO;
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent event) {
        if (serversConnectDAO.existsByDiscordGuildId(event.getGuild().getId())) {
            for (Member member : event.getGuild().getMembers()) {
                if (!usersConnectDAO.existsByDiscordId(member.getId()) && !member.getUser().isBot()) {
                    event.getGuild().getTextChannelsByName("telegram", true).get(0).sendMessage("Register a member with name: " + member.getUser().getName()).queue();
                } else if (usersConnectDAO.existsByDiscordId(member.getId()) && !guildProfileDAO.existsByUserId(usersConnectDAO.getByDiscordId(member.getUser().getId()))) {
                    guildProfileDAO.setGuildProfile(member.getNickname(), usersConnectDAO.getByDiscordName(member.getUser().getName()), serversConnectDAO.getByDiscordGuild(event.getGuild().getId()), profileDAO.getProfile(member.getUser().getName()));
                } else if (guildProfileDAO.existsByUserId(usersConnectDAO.getByDiscordId(member.getId())) && !member.getNickname().equals(guildProfileDAO.getGuildProfileByUsersConnect(usersConnectDAO.getByDiscordId(member.getId())).getName())) {
                    usersConnectDAO.updateByDiscordId(member.getUser().getName(), member.getUser().getId());
                    guildProfileDAO.updateGuildProfile(member.getNickname(), usersConnectDAO.getByDiscordId(member.getId()));
                }
            }
        }
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent.class;
    }
}
