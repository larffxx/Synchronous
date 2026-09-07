package com.larffxx.synchronousdiscord.infrastructure.sender.utility;

import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

/**
 * User In DB Checker class.
 */
@Component
public class UserInDBChecker {
    /**
     * The users connect repository.
     */
    private final UsersConnectRepository usersConnectRepository;

    /**
     * Creates a new UserInDBChecker.
     * @param usersConnectRepository the users connect repository.
     */
    public UserInDBChecker(UsersConnectRepository usersConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
    }

    /**
     * Checks whether user in db.
     * @param matcher the matcher.
     * @param member the member.
     * @return the resulting boolean.
     */
    public boolean isUserInDB(Matcher matcher, Member member){
        return matcher.group().replace("@", "").equals(usersConnectRepository.findByDiscordUserId(member.getId()).getTelegramName());
    }
}
