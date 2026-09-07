package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.CommandPayload;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Command Payload Parser class.
 */
@Component
public class CommandPayloadParser implements Parser<SlashCommandInteractionEvent, CommandPayload>{
    /**
     * The servers connect repository.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a new CommandPayloadParser.
     * @param serversConnectRepository the servers connect repository.
     */
    public CommandPayloadParser(ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Parses event into command payload.
     * @param event the event.
     * @return the resulting command payload.
     */
    @Override
    public CommandPayload parse(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Message message = event.getHook().retrieveOriginal().complete();
        List<String> trackList;

        if(message.getEmbeds().isEmpty()) {
            trackList = new ArrayList<>();
        }else {
            MessageEmbed messageEmbed;

            messageEmbed = message.getEmbeds().get(0);

            trackList = messageEmbed.getFields().stream().map(MessageEmbed.Field::getValue).toList();
        }
        return new CommandPayload(
                guildID,
                Objects.requireNonNull(serversConnectRepository.getConnectByDiscordGuild(guildID).getTelegramChannel()),
                Objects.requireNonNull(event.getInteraction().getMember()).getEffectiveName(),
                event.getName(),
                new ArrayList<>(event.getOptions().stream().map(OptionMapping::getAsString).collect(Collectors.toList())),
                trackList
        );
    }
}
