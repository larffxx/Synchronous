package com.larffxx.synchronousdiscord.service.executor;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.exception.VerifyException;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.service.registry.SlashCommandRegistry;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter
@Setter
public class CommandVerifier {
    private final ServersConnectRepository serversConnectRepository;
    private final SlashCommandRegistry slashCommandRegistry;
    private final EventReceiver eventReceiver;

    public CommandVerifier(SlashCommandRegistry slashCommandRegistry, EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.slashCommandRegistry = slashCommandRegistry;
        this.eventReceiver = eventReceiver;
        this.serversConnectRepository = serversConnectRepository;
    }

    public void verifyCommand(SlashCommandInteractionEvent t) throws VerifyException {
        List<Command.Option> commandOptions = t.getGuild().retrieveCommandById(t.getCommandId()).complete().getOptions();
        List<OptionMapping> options = t.getInteraction().getOptions();

        if (commandOptions.size() > t.getOptions().size()) {
            throw new VerifyException(InfExcMessages.VALUES_NOT_PROVIDED_ERROR);
        }

        eventReceiver.setMessageChannel(t.getMessageChannel());
        eventReceiver.setOptionMappings(options);

        for (OptionMapping option : options) {
            for (Command.Option providedOption : commandOptions) {
                if (option != null && providedOption != null && !option.getType().equals(providedOption.getType())) {
                    throw new VerifyException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
                }
            }

            if (option != null && option.getType().equals(OptionType.INTEGER) && option.getAsInt() < 0) {
                throw new VerifyException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
            }
        }
    }

    public void verifyCommand(JsonNode data) throws VerifyException {
        String telegramChatId = data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText();
        Guild guild = eventReceiver.getJda().getGuildById(serversConnectRepository.getConnectByTelegramChannel(telegramChatId).getDiscordGuild());
        String discordTextChannel = CommandConstants.DISCORD_TEXT_CHANNEL;
        String strCommand = data.findValue(CommandConstants.COMMAND_VALUE).asText();
        Command discordCommand = guild.retrieveCommands().complete().stream()
                .filter(command -> command.getName().equals(strCommand))
                .findFirst().get();

        List<Command.Option> commandOptions = discordCommand.getOptions();
        List<String> providedOptions = data.findValues(CommandConstants.COMMAND_OPTIONS)
                .stream()
                .map(JsonNode::asText).toList();


        if (providedOptions.size() > commandOptions.size()) {
            throw new VerifyException(InfExcMessages.VALUES_NOT_PROVIDED_ERROR);
        }

        eventReceiver.setMessageChannel(guild.getTextChannelsByName(discordTextChannel, true).get(0));

        verifyMappedValues(commandOptions,providedOptions);
    }

    private static void verifyMappedValues(List<Command.Option> options, List<String> providedOptions) throws VerifyException {
        for (Command.Option option : options) {
            for (String providedOption : providedOptions) {
                switch (option.getType()) {
                    case STRING: break;
                    case INTEGER:
                        long number;
                        try {
                            number = Long.parseLong(providedOption);
                            if (number < 0) throw new VerifyException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
                        } catch (NumberFormatException e) {
                            throw new VerifyException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + option.getType());
                }
            }
        }
    }
}
