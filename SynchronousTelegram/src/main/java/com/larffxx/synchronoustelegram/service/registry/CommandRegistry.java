package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.controller.Command;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;


@Component
@AllArgsConstructor
public class CommandRegistry implements Registry<Command> {
    private final Collection<Command> commands;
    private Map<String, Command> commandMap;

    @PostConstruct
    public void mapCommands() {
        for (Command command : commands) {
            commandMap.put(command.getCommand(), command);
        }

    }

    public Command get(String command) {
        return commandMap.get(command);
    }
}
