package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import com.larffxx.synchronousdiscord.infrastructure.discord.AttachmentDownloader;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessageType;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Message Payload Parser class.
 */
@Component
public class MessagePayloadParser implements Parser<MessageReceivedEvent, MessagePayload> {
    /**
     * The servers connect repository.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a new MessagePayloadParser.
     * @param serversConnectRepository the servers connect repository.
     */
    public MessagePayloadParser(ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Parses message received event into message payload.
     * @param messageReceivedEvent the message received event.
     * @return the resulting message payload.
     */
    @Override
    public MessagePayload parse(MessageReceivedEvent messageReceivedEvent) {
        AttachmentDownloader attachmentDownloader = new AttachmentDownloader();
        Long guildID = Objects.requireNonNull(messageReceivedEvent.getGuild()).getIdLong();
        Long telegramChannelId = Long.valueOf(serversConnectRepository.getConnectByDiscordGuild(String.valueOf(guildID)).getTelegramChannel());

        List<File> attachments = attachmentDownloader.downloadAttachments(messageReceivedEvent.getMessage().getAttachments());

        if(!attachments.isEmpty()) {
            return new MessagePayload(
                    guildID,
                    telegramChannelId,
                    messageReceivedEvent.getMessage().getAuthor().getEffectiveName(),
                    Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay()),
                    attachments
            );
        }

        return new MessagePayload(
                guildID,
                telegramChannelId,
                Objects.requireNonNull(messageReceivedEvent.getMessage().getAuthor().getEffectiveName()),
                Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay()),
                attachments, MessageType.TEXT_MESSAGE
        );
    }
}
