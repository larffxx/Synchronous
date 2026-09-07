package com.larffxx.synchronousdiscord.config.bot;

import com.larffxx.synchronousdiscord.infrastructure.listener.EventsListener;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Discord Bot class.
 */
@Component
@Getter
@Setter
public class DiscordBot {
    /**
     * The token.
     */
    @Value("${token}")
    private String token;
    /**
     * The event context.
     */
    private final EventContext eventContext;
    /**
     * The events listener.
     */
    private final EventsListener eventsListener;
    /**
     * The jda.
     */
    private JDA jda;

    /**
     * Creates a new DiscordBot.
     * @param eventContext the event context.
     * @param eventsListener the events listener.
     */
    public DiscordBot(EventContext eventContext, EventsListener eventsListener) {
        this.eventContext = eventContext;
        this.eventsListener = eventsListener;
    }

    /**
     * Setting Up Discord.
     */
    @PostConstruct
    public void settingUpDiscord() {
        jda = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(eventsListener)
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EXPRESSIONS, GatewayIntent.SCHEDULED_EVENTS)
                .build();
        eventContext.setJda(jda);
    }
}
