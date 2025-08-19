package com.larffxx.synchronousdiscord.receiver;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter
@Setter
public class EventReceiver {
    private TextChannel textChannel;
    private MessageChannel messageChannel;
    private List<OptionMapping> optionMappings;
    private JDA jda;
}
