package com.larffxx.synchronousdiscord.service.registry;

import com.larffxx.synchronousdiscord.service.controller.slashcommand.Command;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Maps slash command names to command handlers.
 */
@Component
@AllArgsConstructor
public class SlashCommandRegistry implements Registry<Command> {
    /**
     * All discovered slash command handlers.
     */
    private final Collection<Command> commands;

    /**
     * Lookup of commands by command name.
     */
    private Map<String, Command> commandMap;

    /**
     * Builds the command name to handler lookup after injection.
     */
    @PostConstruct
    public void mapCommands() {
        for (Command command : commands) {
            commandMap.put(command.getCommand(), command);
        }
    }

    /**
     * Returns the command registered under the given name.
     *
     * @param command registered command name
     * @return command handler for the given name
     */
    @Override
    public Command getSender(String command) {
        return commandMap.get(command);
    }

}
