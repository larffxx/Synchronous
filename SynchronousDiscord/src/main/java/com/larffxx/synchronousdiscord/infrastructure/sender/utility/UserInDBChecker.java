package com.larffxx.synchronousdiscord.infrastructure.sender.utility;

import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class UserInDBChecker {
    private final UsersConnectRepository usersConnectRepository;

    public UserInDBChecker(UsersConnectRepository usersConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
    }

    public boolean isUserInDB(Matcher matcher, Member member){
        return matcher.group().replace("@", "").equals(usersConnectRepository.findByDiscordUserId(member.getId()).getTelegramName());
    }
}
