package com.larffxx.synchronousdiscord.config.bot;

import com.larffxx.synchronousdiscord.infrastructure.listener.EventsListener;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import moe.kyokobot.libdave.DaveFactory;
import moe.kyokobot.libdave.NativeDaveFactory;
import moe.kyokobot.libdave.jda.LDJDADaveSessionFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.audio.dave.DaveSessionFactory;
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
    private final EventContext eventContext;
    private final EventsListener eventsListener;
    private JDA jda;

    public DiscordBot(EventContext eventContext, EventsListener eventsListener) {
        this.eventContext = eventContext;
        this.eventsListener = eventsListener;
    }

    @PostConstruct
    public void settingUpDiscord() {
        DaveFactory daveFactory = new NativeDaveFactory();
        DaveSessionFactory daveSessionFactory = new LDJDADaveSessionFactory(daveFactory);

        jda = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT).enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(eventsListener)
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EXPRESSIONS, GatewayIntent.SCHEDULED_EVENTS)
                .setAudioModuleConfig(new AudioModuleConfig()
                        .withDaveSessionFactory(daveSessionFactory))
                .build();
        eventContext.setJda(jda);
    }
}
