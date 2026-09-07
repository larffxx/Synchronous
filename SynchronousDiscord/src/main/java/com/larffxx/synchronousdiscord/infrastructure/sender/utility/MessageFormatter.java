package com.larffxx.synchronousdiscord.infrastructure.sender.utility;

import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Message Formatter class.
 */
@Component
public class MessageFormatter {
    /**
     * The users connect repository.
     */
    private final UsersConnectRepository usersConnectRepository;
    /**
     * The user in db checker.
     */
    private final UserInDBChecker userInDBChecker;

    /**
     * Creates a new MessageFormatter.
     * @param usersConnectRepository the users connect repository.
     * @param userInDBChecker the user in db checker.
     */
    public MessageFormatter(UsersConnectRepository usersConnectRepository, UserInDBChecker userInDBChecker) {
        this.usersConnectRepository = usersConnectRepository;
        this.userInDBChecker = userInDBChecker;
    }

    /**
     * Formats message.
     * @param message the message.
     * @param matcher the matcher.
     * @param memberList the member list.
     * @return the resulting string.
     */
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
