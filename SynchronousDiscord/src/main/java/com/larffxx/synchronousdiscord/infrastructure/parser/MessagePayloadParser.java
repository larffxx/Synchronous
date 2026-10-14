package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import com.larffxx.synchronousdiscord.infrastructure.discord.AttachmentDownloader;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessageType;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
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
     * Downloads message attachments.
     */
    private final AttachmentDownloader attachmentDownloader;

    /**
     * Creates a new MessagePayloadParser.
     * @param serversConnectRepository the servers connect repository.
     * @param attachmentDownloader the attachment downloader.
     */
    public MessagePayloadParser(ServersConnectRepository serversConnectRepository, AttachmentDownloader attachmentDownloader) {
        this.serversConnectRepository = serversConnectRepository;
        this.attachmentDownloader = attachmentDownloader;
    }

    /**
     * Parses message received event into message payload.
     * @param messageReceivedEvent the message received event.
     * @return the resulting message payload.
     */
    @Override
    public MessagePayload parse(MessageReceivedEvent messageReceivedEvent) {
        Long guildID = Objects.requireNonNull(messageReceivedEvent.getGuild()).getIdLong();
        ServersConnect connection = serversConnectRepository.getConnectByDiscordGuild(String.valueOf(guildID));
        if (connection == null) {
            throw new IllegalStateException("Discord guild is not connected to Telegram: " + guildID);
        }
        Long telegramChannelId = Long.valueOf(connection.getTelegramChannel());

        List<File> attachments = attachmentDownloader.downloadAttachments(messageReceivedEvent.getMessage().getAttachments());
        String messageId = messageReceivedEvent.getMessage().getId();
        String channelId = messageReceivedEvent.getChannel().getId();
        String channelName = messageReceivedEvent.getChannel().getName();
        var sentAt = messageReceivedEvent.getMessage().getTimeCreated().toInstant();

        if(!attachments.isEmpty()) {
            return new MessagePayload(
                    guildID,
                    telegramChannelId,
                    messageReceivedEvent.getMessage().getAuthor().getEffectiveName(),
                    Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay()),
                    attachments,
                    MessageType.PHOTO_MESSAGE,
                    messageId,
                    channelId,
                    channelName,
                    sentAt
            );
        }

        return new MessagePayload(
                guildID,
                telegramChannelId,
                Objects.requireNonNull(messageReceivedEvent.getMessage().getAuthor().getEffectiveName()),
                Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay()),
                attachments,
                MessageType.TEXT_MESSAGE,
                messageId,
                channelId,
                channelName,
                sentAt
        );
    }
}
