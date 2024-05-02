package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class CommandPreProcessor implements PreProcessor<Command> {
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
