package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.CommandPayload;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommandPayloadParser implements Parser<SlashCommandInteractionEvent, CommandPayload>{
    private final ServersConnectRepository serversConnectRepository;

    public CommandPayloadParser(ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public CommandPayload parse(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        return new CommandPayload(
                guildID,
                Objects.requireNonNull(serversConnectRepository.getConnectByDiscordGuild(guildID).getTelegramChannel()),
                Objects.requireNonNull(event.getInteraction().getMember()).getEffectiveName(),
                event.getName(),
                new ArrayList<>(event.getOptions().stream().map(OptionMapping::getAsString).collect(Collectors.toList()))
        );
    }
}
