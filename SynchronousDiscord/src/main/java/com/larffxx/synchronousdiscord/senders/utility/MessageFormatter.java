package com.larffxx.synchronousdiscord.senders.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersInfMessages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;

@Component
public class MessageFormatter {
    private final UsersConnectDAO usersConnectDAO;
    private final UserInDBChecker userInDBChecker;

    public MessageFormatter(UsersConnectDAO usersConnectDAO, UserInDBChecker userInDBChecker) {
        this.usersConnectDAO = usersConnectDAO;
        this.userInDBChecker = userInDBChecker;
    }


    public void sendFormattedMessage(JsonNode data, Matcher matcher, TextChannel textChannel) {
        List<Member> memberList = textChannel.getMembers();

        for (Member member : memberList) {
            if (usersConnectDAO.existsByDiscordId(member.getUser().getId())) {
                if (userInDBChecker.isUserInDB(matcher, member)) {
                    String formattedMSG = data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText().replace(matcher.group(), member.getUser().getAsMention());
                    textChannel.sendMessage(formattedMSG).queue();
                }
            }
        }
    }
}
