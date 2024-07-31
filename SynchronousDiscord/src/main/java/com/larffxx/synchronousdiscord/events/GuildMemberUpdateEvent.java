package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.events.utility.UpdateGuildProfiles;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class GuildMemberUpdateEvent extends Event<net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent> {
    private final ServersConnectDAO serversConnectDAO;
    private final GuildProfileDAO guildProfileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final UpdateGuildProfiles updateGuildProfiles;


    public GuildMemberUpdateEvent(EventReceiver eventReceiver, UsersConnectDAO usersConnectDAO, GuildProfileDAO guildProfileDAO, ServersConnectDAO serversConnectDAO, UpdateGuildProfiles updateGuildProfiles) {
        super(eventReceiver);
        this.usersConnectDAO = usersConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
        this.serversConnectDAO = serversConnectDAO;
        this.updateGuildProfiles = updateGuildProfiles;
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent event) {
        Guild guild = event.getGuild();
        if (serversConnectDAO.existsByDiscordGuildId(event.getGuild().getId())) {

            List<Member> members =
                    guild.getMembers()
                            .stream()
                            .filter(member -> usersConnectDAO.existsByDiscordId(member.getId()))
                            .toList();

            if(members.isEmpty()){
                guild.getTextChannelsByName("telegram", true).get(0).sendMessage(InfExcMessages.NO_REGISTERED_USERS).queue();
            }

            updateGuildProfiles.update(members,guild);

        }
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent.class;
    }
}
