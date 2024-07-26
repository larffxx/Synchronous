package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersInfMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.utility.MessageFormatter;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
@Setter
public class MessageSender extends Sender<JsonNode> {
    private final ServersConnectDAO serversConnectDAO;
    private final MessageFormatter messageFormatter;

    public MessageSender(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, MessageFormatter messageFormatter) {
        super(eventReceiver);
        this.serversConnectDAO = serversConnectDAO;
        this.messageFormatter = messageFormatter;
    }

    @Override
    public void send(JsonNode data) {
        Matcher matcher = Pattern.compile(getUSERNAME_PATTER()).matcher(data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(SendersInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersInfMessages.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        if (matcher.find()) {
            messageFormatter.sendFormattedMessage(data,matcher,textChannel);
        } else {
            textChannel.sendMessage(data.findValue(SendersInfMessages.NAME_IN_TELEGRAM).asText() + ": " + data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM)).queue();
        }
    }

    @Override
    public String getSender() {
        return "messageSender";
    }
}
