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
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Member;


@Component
@Getter
@Setter
public class EventsListener implements EventListener {

    private final EventPreProcessor eventPreProcessor;

    public EventsListener(EventPreProcessor eventPreProcessor) {
        this.eventPreProcessor = eventPreProcessor;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (eventPreProcessor.getCommand(event.getClass()) != null) {
            Event<GenericEvent> ev = eventPreProcessor.getCommand(event.getClass());
            ev.execute(event);
        }
    }
}
