package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.infrastructure.discord.Addable;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.stereotype.Component;

/**
 * Delegates guild member join events to the newcomer handler.
 */
@Component
public class MemberJoinEvent implements Event<GuildMemberJoinEvent> {
    /**
     * Delegate that processes a newly joined guild member.
     */
    private Addable<GuildMemberJoinEvent> addable;

    /**
     * Passes the member join event to the configured delegate.
     *
     * @param event guild member join event from JDA
     */
    @Override
    public void execute(GuildMemberJoinEvent event) {
        addable.add(event);
    }

    /**
     * Returns the guild member join event class.
     *
     * @return guild member join event class
     */
    @Override
    public Class getEvent() {
        return GuildMemberJoinEvent.class;
    }
}
