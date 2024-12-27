package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;


@Component
public class CommandPreProcessor implements PreProcessor<Command> {
    private final Collection<Command> commands;
    private Map<String, Command> commandMap;

    @PostConstruct
    public void mapCommands() {
        for (Command command : commands) {
            commandMap.put(command.getCommand(), command);
        }

    }

    public Command getCommand(String command) {
        return commandMap.get(command);
    }

    public Command getCommand(UpdateHolder updateHolder) {
        return commandMap.get(updateHolder.getUpdate().getCallbackQuery().getData());
    }

    public CommandPreProcessor(final Collection<Command> commands, final Map<String, Command> commandMap) {
        this.commands = commands;
        this.commandMap = commandMap;
    }
}
