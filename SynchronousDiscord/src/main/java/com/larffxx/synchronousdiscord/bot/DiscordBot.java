package com.larffxx.synchronousdiscord.bot;

import com.larffxx.synchronousdiscord.listeners.EventsListener;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
@Getter
@Setter
public class DiscordBot {
    @Value("${token}")
    private String token;
    private final EventReceiver eventReceiver;
    private final EventsListener eventsListener;
    private JDA jda;

    public DiscordBot(EventReceiver eventReceiver, EventsListener eventsListener) {
        this.eventReceiver = eventReceiver;
        this.eventsListener = eventsListener;
    }

    @PostConstruct
    public void settingUpDiscord() {
        jda = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT).enableCache(CacheFlag.VOICE_STATE).addEventListeners(eventsListener).build();
        eventReceiver.setJda(jda);
    }
}
