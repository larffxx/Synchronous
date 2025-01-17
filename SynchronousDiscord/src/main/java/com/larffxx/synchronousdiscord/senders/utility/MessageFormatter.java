package com.larffxx.synchronousdiscord.senders.utility;

import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import net.dv8tion.jda.api.entities.Member;
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


    public String formatMessage(String message, Matcher matcher, List<Member> memberList) {
        for (Member member : memberList) {
            if (usersConnectDAO.existsByDiscordId(member.getUser().getId())) {
                if (userInDBChecker.isUserInDB(matcher, member)) {
                    return message.replace(matcher.group(), member.getUser().getAsMention());
                }
            }
        }
        return "No user found";
    }
}
