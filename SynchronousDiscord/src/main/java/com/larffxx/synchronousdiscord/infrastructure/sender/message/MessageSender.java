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

@Component
@Getter
@Setter
public class MessageSender implements Sender<TelegramMessageContext> {
    private final String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
    private final EventContext eventContext;
    private final ServersConnectRepository serversConnectRepository;
    private final MessageFormatter messageFormatter;

    public MessageSender(MessageFormatter messageFormatter, EventContext eventContext, ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
        this.messageFormatter = messageFormatter;
        this.eventContext = eventContext;
    }

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

    @Override
    public String getSender() {
        return "TEXT_MESSAGE";
    }
}
