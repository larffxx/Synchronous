package com.larffxx.synchronousdiscord.domain.context;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Event Context class.
 */
@Component
@Getter
@Setter
public class EventContext {
    /**
     * The jda.
     */
    private JDA jda;
    /**
     * The text channel.
     */
    private TextChannel textChannel;
    /**
     * The message channel.
     */
    private MessageChannel messageChannel;
    /**
     * The option mappings.
     */
    private List<OptionMapping> optionMappings;
}
