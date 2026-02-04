package com.larffxx.synchronousdiscord.application.controller.slashcommand;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.exception.command.DeleteMessageException;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class DeleteMessageCommand implements Command {
    private final EventReceiver eventReceiver;

    public DeleteMessageCommand(EventReceiver eventReceiver) {
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws DeleteMessageException {
        MessageChannel messageChannel = eventReceiver.getMessageChannel();
        int amount = eventReceiver.getOptionMappings().get(0).getAsInt();

        CompletableFuture<Void> completableFuture = deleteMessages(messageChannel, amount);

        completableFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                event.getHook().editOriginal(ex.getMessage()).queue();
            } else {
                event.getHook().editOriginal(String.format(CommandConstants.DELETE_SUCCESS_MESSAGE, amount)).queue();
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
