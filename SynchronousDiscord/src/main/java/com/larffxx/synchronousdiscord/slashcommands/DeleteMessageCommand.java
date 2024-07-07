package com.larffxx.synchronousdiscord.slashcommands;


import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;


@Component
public class DeleteMessageCommand extends Command{
    private int NUM;
    public DeleteMessageCommand(EventReceiver eventReceiver) {
        super(eventReceiver);
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) {
        for (Message message : t.getMessageChannel().getIterableHistory().takeAsync(t.getOption("value").getAsInt()).join()){
            t.getMessageChannel().deleteMessageById(message.getId()).queue();
        }
        t.reply("Messages was deleted").queue();
    }

    @Override
    public void execute(JsonNode data) {

    }


    @Override
    public String getCommand() {
        return "delete";
    }
}
