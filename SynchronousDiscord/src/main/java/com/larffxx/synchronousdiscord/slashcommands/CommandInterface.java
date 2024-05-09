package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandInterface{
    void execute(SlashCommandInteractionEvent t);
    void execute(JsonNode data);
    String getCommand();

}
