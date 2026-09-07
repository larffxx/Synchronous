package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.repo.GuildProfileRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rewrites Discord style user mentions into Telegram names.
 * Replaces at-mentions with the linked Telegram name when the user is registered.
 */
@Getter
@Setter
@Component
public class UserMentionHandler {
    /**
     * Matcher for the most recent mention search performed on a message.
     */
    private Matcher matcher;
    /**
     * Repository used to look up registered profiles by Discord name.
     */
    private final GuildProfileRepository guildProfileRepository;

    /**
     * Creates the handler with the profile repository.
     * @param guildProfileRepository repository for profile lookup
     */
    public UserMentionHandler(GuildProfileRepository guildProfileRepository) {
        this.guildProfileRepository = guildProfileRepository;
    }

    /**
     * Prefixes the message with the sender name and converts Discord mentions to Telegram names.
     * @param messageContext the synced message context
     * @return the formatted message text
     */
    public String convertMentionsToTelegramNames(MessageContext messageContext) {
        setMatcher(messageContext.getMessage());

        if(matcher.find()) {
           return formatMessageWithMention(messageContext.getMessage());
        }
        return String.format("%s: %s", messageContext.getUser(), messageContext.getMessage());
    }

    /**
     * Replaces the found mention with the linked Telegram name.
     * @param msg the original message text
     * @return the message text with the mention replaced
     */
    private String formatMessageWithMention(String msg){
        String discordName = matcher.group().replace("@","");
        return msg.replace(matcher.group(),
                "@" + guildProfileRepository.getByName(discordName).getUsersConnect().getTelegramName());
    }

    /**
     * Compiles the mention pattern and matches it against the given message.
     * @param msg the message text to search
     */
    private void setMatcher(String msg){
        String USERNAME_PATTER = "@([a-zA-Z0-9._\\-]{3,})";
        matcher = Pattern.compile(USERNAME_PATTER).matcher(msg);
    }
}
