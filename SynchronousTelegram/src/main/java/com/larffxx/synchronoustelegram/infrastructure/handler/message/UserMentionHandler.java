package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.GuildProfileRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Component
public class UserMentionHandler {
    private Matcher matcher;
    private final GuildProfileRepository guildProfileRepository;

    public UserMentionHandler(GuildProfileRepository guildProfileRepository) {
        this.guildProfileRepository = guildProfileRepository;
    }

    public String convertMentionsToTelegramNames(DiscordPayload payload) {
        setMatcher(payload.getMessage());

        if(matcher.find()) {
           return formatMessageWithMention(payload.getMessage());
        }
        return String.format("%s: %s", payload.getAuthor(), payload.getMessage());
    }

    private String formatMessageWithMention(String msg){
        String discordName = matcher.group().replace("@","");
        return msg.replace(matcher.group(),
                "@" + guildProfileRepository.getByName(discordName).getUsersConnect().getTelegramName());
    }

    private void setMatcher(String msg){
        String USERNAME_PATTER = "@([a-zA-Z0-9._\\-]{3,})";
        matcher = Pattern.compile(USERNAME_PATTER).matcher(msg);
    }
}
