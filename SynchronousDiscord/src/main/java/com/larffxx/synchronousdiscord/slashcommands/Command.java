package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class Command {
    private final EventReceiver eventReceiver;

    public Command(EventReceiver eventReceiver) {
        this.eventReceiver = eventReceiver;
    }
    public abstract void execute(SlashCommandInteractionEvent t) throws CommandException;
    public abstract void execute(JsonNode data) throws CommandException;
    public abstract String getCommand();
}
