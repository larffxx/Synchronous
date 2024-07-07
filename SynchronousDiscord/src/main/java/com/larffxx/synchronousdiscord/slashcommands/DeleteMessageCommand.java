package com.larffxx.synchronousdiscord.slashcommands;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class DeleteMessageCommand extends Command {

    private static final String VALUE_OPTION = "value";

    public DeleteMessageCommand(EventReceiver eventReceiver) {
        super(eventReceiver);
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) throws CommandException {

        if (t.getOptions().isEmpty()) {
            throw new CommandException(InfExcMessages.VALUES_NOT_PROVIDED_ERROR);
        }

        MessageChannel messageChannel = t.getMessageChannel();
        OptionMapping optionMapping = t.getOption(VALUE_OPTION);

        if (optionMapping == null || !optionMapping.getType().equals(OptionType.INTEGER)) {
            throw new CommandException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
        }

        int amount = optionMapping.getAsInt();

        CompletableFuture<Void> completableFuture = deleteMessages(messageChannel, amount);

        t.reply(String.format("Marked %s messages for deletion", amount)).queue();

        completableFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                t.reply(ex.getMessage()).queue();
            } else {
                t.reply(String.format("Successfully deleted %s messages", amount)).queue();
            }
        });
    }

    @Override
    public void execute(JsonNode data) {

    }

    CompletableFuture<Void> deleteMessages(MessageChannel channel, int amount) {
        return channel.getIterableHistory()
                .takeAsync(amount)
                .thenAccept(channel::purgeMessages);
    }

    @Override
    public String getCommand() {
        return "delete";
    }
}
