package com.larffxx.synchronousdiscord.infrastructure.parser;

import com.larffxx.synchronousdiscord.infrastructure.payload.MessagePayload;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class MessagePayloadParser implements Parser<MessageReceivedEvent, MessagePayload> {
    @Override
    public MessagePayload parse(MessageReceivedEvent messageReceivedEvent) {
        return new MessagePayload(
                messageReceivedEvent.getGuild().getIdLong(),
                Objects.requireNonNull(messageReceivedEvent.getMessage().getAuthor().getEffectiveName()),
                Objects.requireNonNull(messageReceivedEvent.getMessage().getContentDisplay())
        );
    }
}
