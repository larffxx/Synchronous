package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersInfMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
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
public class MessageSender extends Sender<JsonNode> {
    private final UsersConnectDAO usersConnectDAO;
    private final ServersConnectDAO serversConnectDAO;

    public MessageSender(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, UsersConnectDAO usersConnectDAO) {
        super(eventReceiver);
        this.serversConnectDAO = serversConnectDAO;
        this.usersConnectDAO = usersConnectDAO;
    }

    @Override
    public void send(JsonNode data) {
        Matcher matcher = Pattern.compile(getUSERNAME_PATTER()).matcher(data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(SendersInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersInfMessages.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        if (matcher.find()) {
            sendFormattedMessage(data, matcher, textChannel);
        } else {
            textChannel.sendMessage(data.findValue(SendersInfMessages.NAME_IN_TELEGRAM).asText() + ": " + data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM)).queue();
        }
    }

    private void sendFormattedMessage(JsonNode data, Matcher matcher, TextChannel textChannel) {
        List<Member> memberList = textChannel.getMembers();

        for (Member member : memberList) {
            if (usersConnectDAO.existsByDiscordId(member.getUser().getId())) {
                if (isUserInDB(matcher, member)) {
                    String formattedMSG = data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText().replace(matcher.group(), member.getUser().getAsMention());
                    textChannel.sendMessage(formattedMSG).queue();
                }
            }
        }
    }

    private boolean isUserInDB(Matcher matcher, Member member){
        return matcher.group().replace("@", "").equals(usersConnectDAO.getByDiscordId(member.getId()).getTelegramName());
    }

    @Override
    public String getSender() {
        return "messageSender";
    }
}
