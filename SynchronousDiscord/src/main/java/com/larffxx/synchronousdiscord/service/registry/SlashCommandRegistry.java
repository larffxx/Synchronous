package com.larffxx.synchronousdiscord.service.registry;

import com.larffxx.synchronousdiscord.service.controller.slashcommand.Command;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class SlashCommandRegistry implements Registry<Command> {
    private final Collection<Command> commands;

    private Map<String, Command> commandMap;

    @PostConstruct
    public void mapCommands() {
        for (Command command : commands) {
            commandMap.put(command.getCommand(), command);
        }
    }

    @Override
    public Command getCommand(String command) {
        return commandMap.get(command);
    }

}
