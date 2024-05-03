package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ReadyEvent extends Event<net.dv8tion.jda.api.events.session.ReadyEvent> {
    public ReadyEvent(EventReceiver eventReceiver) {
        super(eventReceiver);
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.session.ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            guild.updateCommands().addCommands(
                    Commands.slash("profile", "get profile"),
                    Commands.slash("play", "play music").addOption(OptionType.STRING, "type", "track url"),
                    Commands.slash("queue", "queue tracks"),
                    Commands.slash("skip", "skip track"),
                    Commands.slash("stop", "stop track"),
                    Commands.slash("loop", "loop track"),
                    Commands.slash("register", "register your telegram").addOption(OptionType.STRING, "telegram", "Enter your Telegram UserName"),
                    Commands.slash("create", "Creating your custom profile")
                            .addOption(OptionType.STRING, "description", "Enter description")
                            .addOption(OptionType.ATTACHMENT, "photo", "Upload photo")
                            .addOption(OptionType.STRING, "url", "Enter your social url"),
                    Commands.slash("edit", "Edit your custom profile")
                            .addOption(OptionType.STRING, "description", "Enter description")
                            .addOption(OptionType.ATTACHMENT, "photo", "Upload photo")
                            .addOption(OptionType.STRING, "url", "Enter your social url"),
                    Commands.slash("connect", "Connect your servers")
                            .addOption(OptionType.STRING, "telegram", "Enter your telegram channel name")
                            .setDefaultPermissions(DefaultMemberPermissions.ENABLED)).queue();
        }
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.session.ReadyEvent.class;
    }

}
