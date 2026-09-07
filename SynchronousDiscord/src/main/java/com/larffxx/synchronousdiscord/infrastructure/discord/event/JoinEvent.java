package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.springframework.stereotype.Component;

/**
 * Sends a greeting message when the bot joins a new guild.
 */
@Component
@Getter
@Setter
public class JoinEvent implements Event<GuildJoinEvent> {

    /**
     * Sends setup instructions to the first text channel of the joined guild.
     *
     * @param event guild join event from JDA
     */
    @Override
    public void execute(GuildJoinEvent event) {
        event.getGuild().getTextChannels().get(0)
                .sendMessage("Hello, i'm here, connect yours telegram using /connect + telegram text channel name, and create text channel with 'telegram' name\n" +
                        "Register your telegram using /register + telegram name").queue();
    }

    /**
     * Returns the guild join event class.
     *
     * @return guild join event class
     */
    @Override
    public Class getEvent() {
        return GuildJoinEvent.class;
    }
}
