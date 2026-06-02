package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import com.larffxx.synchronousdiscord.infrastructure.discord.AttachmentDownloader;
import com.larffxx.synchronousdiscord.infrastructure.payload.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MessagePayloadParser implements Parser<MessageReceivedEvent, MessagePayload> {

    @Override
    public MessagePayload parse(MessageReceivedEvent messageReceivedEvent) {
        AttachmentDownloader attachmentDownloader = new AttachmentDownloader();

        List<File> attachments = attachmentDownloader.downloadAttachments(messageReceivedEvent.getMessage().getAttachments());

        if(!attachments.isEmpty()) {
            return new MessagePayload(
                    messageReceivedEvent.getGuild().getIdLong(),
                    Objects.requireNonNull(messageReceivedEvent.getMessage().getAuthor().getEffectiveName()),
                    Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay()),
                    attachments
            );
        }

        return new MessagePayload(
                messageReceivedEvent.getGuild().getIdLong(),
                Objects.requireNonNull(messageReceivedEvent.getMessage().getAuthor().getEffectiveName()),
                Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay()),
                attachments, MessageType.TEXT_MESSAGE
        );
    }
}
