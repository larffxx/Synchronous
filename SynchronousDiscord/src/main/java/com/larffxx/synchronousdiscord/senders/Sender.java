package com.larffxx.synchronousdiscord.senders;

import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class Sender <T> {
    private final String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
    private final EventReceiver eventReceiver;

    public Sender(EventReceiver eventReceiver) {
        this.eventReceiver = eventReceiver;
    }

    public abstract void send(T data);
    public abstract String getSender();
}
