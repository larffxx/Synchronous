package com.larffxx.synchronousdiscord.infrastructure.sender.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import com.larffxx.synchronousdiscord.infrastructure.sender.utility.MessageFormatter;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
@Setter
public class MessageSender implements Sender<JsonNode> {
    private final String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;
    private final MessageFormatter messageFormatter;

    public MessageSender(MessageFormatter messageFormatter, EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
        this.messageFormatter = messageFormatter;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void send(JsonNode data) {
        Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(data.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda()
                .getGuildById(serversConnectRepository.getConnectByTelegramChannel(data.findValue(SendersConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersConstants.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        List<Member> memberList = textChannel.getMembers();
        String message = data.findValue(SendersConstants.NAME_IN_TELEGRAM).asText()+": "+ data.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText();

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
