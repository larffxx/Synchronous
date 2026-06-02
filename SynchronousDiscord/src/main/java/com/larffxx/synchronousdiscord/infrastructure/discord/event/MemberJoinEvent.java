package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.infrastructure.discord.Addable;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.stereotype.Component;

@Component
public class MemberJoinEvent implements Event<GuildMemberJoinEvent> {
    private Addable<GuildMemberJoinEvent> addable;

    @Override
    public void execute(GuildMemberJoinEvent event) {
        addable.add(event);
    }

    @Override
    public Class getEvent() {
        return GuildMemberJoinEvent.class;
    }
}
