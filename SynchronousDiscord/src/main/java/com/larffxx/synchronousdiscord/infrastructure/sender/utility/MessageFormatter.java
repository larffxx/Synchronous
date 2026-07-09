package com.larffxx.synchronousdiscord.infrastructure.sender.utility;

import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;

@Component
public class MessageFormatter {
    private final UsersConnectRepository usersConnectRepository;
    private final UserInDBChecker userInDBChecker;

    public MessageFormatter(UsersConnectRepository usersConnectRepository, UserInDBChecker userInDBChecker) {
        this.usersConnectRepository = usersConnectRepository;
        this.userInDBChecker = userInDBChecker;
    }


    public String formatMessage(String message, Matcher matcher, List<Member> memberList) {
        for (Member member : memberList) {
            if (usersConnectRepository.existsByDiscordUserId(member.getUser().getId())) {
                if (userInDBChecker.isUserInDB(matcher, member)) {
                    return message.replace(matcher.group(), member.getUser().getAsMention());
                }
            }
        }
        return "No user found";
    }
}
