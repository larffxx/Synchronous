package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

/**
 * Registers slash commands on every guild once JDA is ready.
 */
@Component
@Getter
@Setter
public class ReadyEvent implements Event<net.dv8tion.jda.api.events.session.ReadyEvent> {

    /**
     * Registers the bot slash commands for all known guilds.
     *
     * @param event JDA ready event
     */
    @Override
    public void execute(net.dv8tion.jda.api.events.session.ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            guild.updateCommands().addCommands(
                    Commands.slash("register", "register your telegram").addOption(OptionType.STRING, "telegram", "Enter your Telegram UserName"),
                    Commands.slash("connect", "Connect your servers")
                            .addOption(OptionType.STRING, "telegram", "Enter your telegram channel name"),
                    Commands.slash("delete", "delete messages")
                            .addOption(OptionType.INTEGER, "value", "Number of messages")
                            .setDefaultPermissions(DefaultMemberPermissions.ENABLED)).queue();
        }
    }

    /**
     * Returns the JDA ready event class.
     *
     * @return JDA ready event class
     */
    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.session.ReadyEvent.class;
    }

}
