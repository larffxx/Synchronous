package com.larffxx.synchronousdiscord.senders.utility;

import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class UserInDBChecker {
    private final UsersConnectDAO usersConnectDAO;

    public UserInDBChecker(UsersConnectDAO usersConnectDAO) {
        this.usersConnectDAO = usersConnectDAO;
    }

    public boolean isUserInDB(Matcher matcher, Member member){
        return matcher.group().replace("@", "").equals(usersConnectDAO.getByDiscordId(member.getId()).getTelegramName());
    }
}
