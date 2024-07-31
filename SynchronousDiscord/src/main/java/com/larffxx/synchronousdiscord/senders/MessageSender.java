package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersInfMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.utility.MessageFormatter;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ServersConnectDAO serversConnectDAO;
    private final MessageFormatter messageFormatter;

    public MessageSender(ServersConnectDAO serversConnectDAO, MessageFormatter messageFormatter, EventReceiver eventReceiver) {
        this.serversConnectDAO = serversConnectDAO;
        this.messageFormatter = messageFormatter;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void send(JsonNode data) {
        Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(SendersInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersInfMessages.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        List<Member> memberList = textChannel.getMembers();
        String message = data.findValue(SendersInfMessages.NAME_IN_TELEGRAM).asText()+": "+ data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText();

        if (matcher.find()) {
            textChannel.sendMessage(messageFormatter.formatMessage(message,matcher,memberList)).queue();
        } else {
            textChannel.sendMessage(message).queue();
        }
    }

    @Override
    public String getSender() {
        return "message";
    }
}
