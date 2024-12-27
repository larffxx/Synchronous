package com.larffxx.synchronousdiscord.parser;

import com.larffxx.synchronousdiscord.payload.MessagePayload;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessagePayloadParser implements Parser<MessageReceivedEvent, MessagePayload> {
    @Override
    public MessagePayload parse(MessageReceivedEvent messageReceivedEvent) {

        return null;
    }
}
