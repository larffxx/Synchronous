package com.larffxx.synchronousdiscord.infrastructure.sender.message;

import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import com.larffxx.synchronousdiscord.infrastructure.sender.utility.MessageFormatter;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Text Message Sender class.
 */
@Component
@Getter
@Setter
public class TextMessageSender implements Sender<TelegramMessageContext> {
    /**
     * The username patter.
     */
    private final String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
    /**
     * The event context.
     */
    private final EventContext eventContext;
    /**
     * The servers connect repository.
     */
    private final ServersConnectRepository serversConnectRepository;
    /**
     * The message formatter.
     */
    private final MessageFormatter messageFormatter;

    /**
     * Creates a new TextMessageSender.
     * @param messageFormatter the message formatter.
     * @param eventContext the event context.
     * @param serversConnectRepository the servers connect repository.
     */
    public TextMessageSender(MessageFormatter messageFormatter, EventContext eventContext, ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
        this.messageFormatter = messageFormatter;
        this.eventContext = eventContext;
    }

    /**
     * Sends telegram message context.
     * @param telegramMessageContext the telegram message context.
     */
    @Override
    public void send(TelegramMessageContext telegramMessageContext) {
        Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(telegramMessageContext.message());
        TextChannel textChannel = telegramMessageContext.textChannel();

        List<Member> memberList = textChannel.getMembers();
        String message = telegramMessageContext.username()+": "+ telegramMessageContext.message();

        if (matcher.find()) {
            textChannel.sendMessage(messageFormatter.formatMessage(message,matcher,memberList)).queue();
        } else {
            textChannel.sendMessage(message).queue();
        }
    }

    /**
     * Returns sender.
     * @return the resulting string.
     */
    @Override
    public String getSender() {
        return "TEXT_MESSAGE";
    }
}
