package com.larffxx.synchronousdiscord.slashcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandInterface{
    void execute(SlashCommandInteractionEvent t);
    void execute(String commandName);
    String getCommand();

}
