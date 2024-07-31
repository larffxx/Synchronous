package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.exception.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public interface Command {

    void execute(SlashCommandInteractionEvent t) throws CommandException;
    void execute(JsonNode data) throws CommandException;
    String getCommand();
}
