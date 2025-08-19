package com.larffxx.synchronousdiscord.sender.utility;

import com.larffxx.synchronousdiscord.repo.UsersConnectRepository;
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
        return matcher.group().replace("@", "").equals(usersConnectRepository.findByDiscordId(member.getId()).getTelegramName());
    }
}
