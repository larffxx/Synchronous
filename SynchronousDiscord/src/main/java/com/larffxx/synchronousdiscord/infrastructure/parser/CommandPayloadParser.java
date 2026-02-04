package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.CommandPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandPayloadParser implements Parser<SlashCommandInteractionEvent, CommandPayload>{
    @Override
    public CommandPayload parse(SlashCommandInteractionEvent event) {
        return new CommandPayload(
                Objects.requireNonNull(event.getInteraction().getGuild()).getId(),
                Objects.requireNonNull(event.getInteraction().getMember()).getEffectiveName(),
                event.getName(),
                new ArrayList<>(event.getOptions().stream().map(OptionMapping::getAsString).collect(Collectors.toList()))
        );
    }
}
