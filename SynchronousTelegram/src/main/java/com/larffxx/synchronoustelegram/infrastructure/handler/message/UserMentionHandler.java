package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.domain.model.Profile;
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
     * Precompiled username mention pattern.
     */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("@([a-zA-Z0-9._\\-]{3,})");
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
        Matcher matcher = USERNAME_PATTERN.matcher(messageContext.getMessage());
        StringBuffer result = new StringBuffer();
        boolean replaced = false;
        while (matcher.find()) {
            String discordName = matcher.group().replace("@", "");
            Profile profile = guildProfileRepository.getByName(discordName);
            String replacement = matcher.group();
            if (profile != null && profile.getUsersConnect() != null
                    && profile.getUsersConnect().getTelegramName() != null) {
                replacement = "@" + profile.getUsersConnect().getTelegramName();
                replaced = true;
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        String text = replaced ? result.toString() : messageContext.getMessage();
        return String.format("%s: %s", messageContext.getUser(), text);
    }
}
