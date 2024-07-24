package com.larffxx.synchronousdiscord.slashcommands;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class DeleteMessageCommand extends Command {

    public DeleteMessageCommand(EventReceiver eventReceiver) {
        super(eventReceiver);
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) throws CommandException {
        MessageChannel messageChannel = getEventReceiver().getMessageChannel();
        int amount = getEventReceiver().getOptionMappings().get(0).getAsInt();


        CompletableFuture<Void> completableFuture = deleteMessages(messageChannel, amount);

        completableFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                t.reply(ex.getMessage()).queue();
            } else {
                t.reply(String.format(CommandInfMessages.DELETE_SUCCESS_MESSAGE, amount)).queue();
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
