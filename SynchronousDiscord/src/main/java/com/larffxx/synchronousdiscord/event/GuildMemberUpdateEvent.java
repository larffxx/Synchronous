package com.larffxx.synchronousdiscord.event;

import com.larffxx.synchronousdiscord.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.repo.UsersConnectRepository;
import com.larffxx.synchronousdiscord.updater.GuildProfileUpdater;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class GuildMemberUpdateEvent implements Event<net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent> {
    private final ServersConnectRepository serversConnectRepository;
    private final UsersConnectRepository usersConnectRepository;
    private final GuildProfileUpdater guildProfileUpdater;


    public GuildMemberUpdateEvent(ServersConnectRepository serversConnectRepository, UsersConnectRepository usersConnectRepository, GuildProfileUpdater guildProfileUpdater) {
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
        this.guildProfileUpdater = guildProfileUpdater;
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent event) {
        Guild guild = event.getGuild();
        if (serversConnectRepository.existsByDiscordGuild(event.getGuild().getId())) {
            List<Member> members =
                    guild.getMembers()
                            .stream()
                            .filter(member -> usersConnectRepository.existsByDiscordId(member.getId()))
                            .toList();

            if(members.isEmpty()){
                guild.getTextChannelsByName("telegram", true).get(0).sendMessage(InfExcMessages.NO_REGISTERED_USERS).queue();
            }

            guildProfileUpdater.update(members,guild);

        }else {
            guild.getTextChannels().get(0).sendMessage(InfExcMessages.SERVER_IS_NOT_CONNECTED_TO_TELEGRAM).queue();
        }
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent.class;
    }
}
