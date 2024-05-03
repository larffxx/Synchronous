package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class JoinEvent extends Event<GuildJoinEvent> {

    public JoinEvent(EventReceiver eventReceiver) {
        super(eventReceiver);
    }

    @Override
    public void execute(GuildJoinEvent event) {
        event.getGuild().getTextChannels().get(0)
                .sendMessage("Hello, i'm here, connect yours telegram using /connect + telegram text channel name, and create text channel with 'telegram' name\n" +
                        "Register your telegram using /register + telegram name").queue();
    }

    @Override
    public Class getEvent() {
        return GuildJoinEvent.class;
    }
}
