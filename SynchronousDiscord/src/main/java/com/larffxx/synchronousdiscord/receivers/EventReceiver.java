package com.larffxx.synchronousdiscord.receivers;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class EventReceiver {
    private TextChannel textChannel;
    private JDA jda;
}
