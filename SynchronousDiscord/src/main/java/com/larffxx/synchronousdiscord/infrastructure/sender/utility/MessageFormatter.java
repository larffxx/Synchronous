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
     * Creates a new MessageFormatter.
     * @param usersConnectRepository the users connect repository.
     */
    public MessageFormatter(UsersConnectRepository usersConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
    }

    /**
     * Formats message, replacing every registered mention with a Discord mention.
     * @param message the message.
     * @param matcher the matcher.
     * @param memberList the member list.
     * @return the resulting string, unchanged when nothing was replaced.
     */
    public String formatMessage(String message, Matcher matcher, List<Member> memberList) {
        StringBuffer result = new StringBuffer();
        boolean replaced = false;
        while (matcher.find()) {
            String mentionName = matcher.group().replace("@", "");
            String replacement = matcher.group();
            for (Member member : memberList) {
                if (usersConnectRepository.existsByDiscordUserId(member.getUser().getId())
                        && mentionName.equals(usersConnectRepository.findByDiscordUserId(member.getId()).getTelegramName())) {
                    replacement = member.getUser().getAsMention();
                    replaced = true;
                    break;
                }
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return replaced ? result.toString() : message;
    }
}
