package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.controller.slashcommand.Command;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;


/**
 * Registry that looks up slash commands by name.
 */
@Component
@AllArgsConstructor
public class CommandRegistry implements Registry<Command> {
    /**
     * All command beans discovered by Spring.
     */
    private final Collection<Command> commands;
    /**
     * Command lookup map keyed by command name.
     */
    private Map<String, Command> commandMap;

    /**
     * Indexes all commands by their name after construction.
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
     * @param command command name to look up
     * @return matching command or null if none is registered
     */
    public Command get(String command) {
        return commandMap.get(command);
    }
}
