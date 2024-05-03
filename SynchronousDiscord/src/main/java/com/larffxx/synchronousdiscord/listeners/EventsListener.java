package com.larffxx.synchronousdiscord.listeners;

import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.events.Event;
import com.larffxx.synchronousdiscord.events.EventPreProcessor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class EventsListener implements EventListener {
    private ServersConnectDAO serversConnectDAO;
    private GuildProfileDAO guildProfileDAO;
    private UsersConnectDAO usersConnectDAO;
    private ProfileDAO profileDAO;
    private final EventPreProcessor eventPreProcessor;

    public EventsListener(ServersConnectDAO serversConnectDAO, GuildProfileDAO guildProfileDAO, UsersConnectDAO usersConnectDAO, EventPreProcessor eventPreProcessor) {
        this.serversConnectDAO = serversConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.eventPreProcessor = eventPreProcessor;
    }

    @Override
    public void onEvent(GenericEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            if (serversConnectDAO.existsByDiscordGuildId(guild.getId())) {
                for (Member member : guild.getMembers()) {
                    if (!usersConnectDAO.existsByDiscordId(member.getId()) && !member.getUser().isBot()) {
                        guild.getTextChannelsByName("telegram", true).get(0).sendMessage("Register a member with name: " + member.getUser().getName()).queue();
                    } else {
                        if (usersConnectDAO.existsByDiscordId(member.getId()) && !guildProfileDAO.existsByUserId(usersConnectDAO.getByDiscordId(member.getUser().getId()))) {
                            guildProfileDAO.setGuildProfile(member.getNickname(), usersConnectDAO.getByDiscordName(member.getUser().getName()), serversConnectDAO.getByDiscordGuild(guild.getId()), profileDAO.getProfile(member.getUser().getName()));
                        } else {
                            if (guildProfileDAO.existsByUserId(usersConnectDAO.getByDiscordId(member.getId())) && !member.getNickname().equals(guildProfileDAO.getGuildProfileByUsersConnect(usersConnectDAO.getByDiscordId(member.getId())).getName())) {
                                guildProfileDAO.updateGuildProfile(member.getNickname(), usersConnectDAO.getByDiscordId(member.getId()));
                            }
                        }
                    }
                }
            }
        }

        if (eventPreProcessor.getCommand(event.getClass()) != null) {
            Event<GenericEvent> ev = eventPreProcessor.getCommand(event.getClass());
            ev.execute(event);
        }

    }
}
