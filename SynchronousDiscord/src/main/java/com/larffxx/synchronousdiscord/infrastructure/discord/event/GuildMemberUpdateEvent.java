package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.domain.context.MemberContext;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.MemberContextResolver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronousdiscord.service.GuildProfileUpdaterService;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Refreshes stored guild profiles when a guild member is updated.
 */
@Component
@Getter
@Setter
public class GuildMemberUpdateEvent implements Event<net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent> {
    /**
     * Repository for Discord to Telegram server connections.
     */
    private final ServersConnectRepository serversConnectRepository;
    /**
     * Repository for Discord to Telegram user connections.
     */
    private final UsersConnectRepository usersConnectRepository;
    /**
     * Service that creates or updates stored guild profiles.
     */
    private final GuildProfileUpdaterService guildProfileUpdaterService;
    /**
     * Resolver that builds a member context from a guild member.
     */
    private final MemberContextResolver memberContextResolver;


    /**
     * Creates a handler for guild member update events.
     *
     * @param serversConnectRepository repository for server connections
     * @param usersConnectRepository repository for user connections
     * @param guildProfileUpdaterService service for profile updates
     * @param memberContextResolver resolver for member contexts
     */
    public GuildMemberUpdateEvent(ServersConnectRepository serversConnectRepository, UsersConnectRepository usersConnectRepository, GuildProfileUpdaterService guildProfileUpdaterService, MemberContextResolver memberContextResolver) {
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
        this.guildProfileUpdaterService = guildProfileUpdaterService;
        this.memberContextResolver = memberContextResolver;
    }

    /**
     * Collects registered members and refreshes their stored profiles.
     *
     * @param event guild member update event from JDA
     */
    @Override
    public void execute(net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent event) {
        Guild guild = event.getGuild();
        if (serversConnectRepository.existsByDiscordGuild(event.getGuild().getId())) {
            List<Member> allMembers = guild.getMembers();
            Set<String> registeredIds = usersConnectRepository.findAllByDiscordUserIdIn(
                    allMembers.stream().map(Member::getId).toList()).stream()
                    .map(UsersConnect::getDiscordUserId)
                    .collect(Collectors.toSet());

            List<MemberContext> members = allMembers
                    .stream()
                    .filter(member -> registeredIds.contains(member.getId()))
                    .map(memberContextResolver::resolveMemberContext)
                    .toList();

            if(members.isEmpty()){
                guild.getTextChannelsByName("telegram", true).get(0).sendMessage(InfExcMessages.NO_REGISTERED_USERS).queue();
            }

            guildProfileUpdaterService.update(members);

        }else {
            guild.getTextChannels().get(0).sendMessage(InfExcMessages.SERVER_IS_NOT_CONNECTED_TO_TELEGRAM).queue();
        }
    }

    /**
     * Returns the JDA guild member update event class.
     *
     * @return guild member update event class
     */
    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent.class;
    }
}
