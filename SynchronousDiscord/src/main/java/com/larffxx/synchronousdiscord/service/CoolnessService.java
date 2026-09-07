package com.larffxx.synchronousdiscord.service;

import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks user coolness ratings, mirrors them as stars in Discord nicknames,
 * and scores chat messages automatically.
 */
@Component
public class CoolnessService {
    private static final Logger log = LoggerFactory.getLogger(CoolnessService.class);
    /**
     * Maximum coolness rating.
     */
    public static final int MAX_COOLNESS = 100;
    /**
     * Coolness points per nickname star.
     */
    public static final int COOLNESS_PER_STAR = 20;
    /**
     * Maximum nickname stars.
     */
    public static final int MAX_STARS = 5;

    private final UsersConnectRepository usersConnectRepository;
    /**
     * Last message content per Discord user id, for duplicate detection.
     */
    private final Map<String, String> lastMessageByUser = new ConcurrentHashMap<>();
    /**
     * Last applied star count per Discord user id, to avoid redundant nickname updates.
     */
    private final Map<String, Integer> appliedStarsByMember = new ConcurrentHashMap<>();

    /**
     * Creates a coolness service.
     * @param usersConnectRepository repository for user connections
     */
    public CoolnessService(UsersConnectRepository usersConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
    }

    /**
     * Returns the coolness of a registered user.
     * @param discordUserId Discord user id
     * @return coolness rating
     * @throws CommandException when the user is not registered
     */
    public int getCoolness(String discordUserId) {
        UsersConnect usersConnect = usersConnectRepository.findByDiscordUserId(discordUserId);
        if (usersConnect == null) {
            throw new CommandException("User is not registered");
        }
        return usersConnect.getCoolness();
    }

    /**
     * Returns the coolness of the user linked to a Telegram name.
     * @param telegramName Telegram name
     * @return coolness rating
     * @throws CommandException when the user is not registered
     */
    public int getCoolnessByTelegramName(String telegramName) {
        UsersConnect usersConnect = usersConnectRepository.findByTelegramName(telegramName);
        if (usersConnect == null) {
            throw new CommandException("User is not registered");
        }
        return usersConnect.getCoolness();
    }

    /**
     * Adds coolness to a member and refreshes the nickname stars.
     * @param member guild member to rate
     * @param delta points to add, may be negative
     * @return new total rating
     * @throws CommandException when the user is not registered
     */
    public int addCoolness(Member member, int delta) {
        UsersConnect usersConnect = usersConnectRepository.findByDiscordUserId(member.getId());
        if (usersConnect == null) {
            throw new CommandException("User is not registered");
        }
        int total = Math.min(MAX_COOLNESS, Math.max(0, usersConnect.getCoolness() + delta));
        usersConnect.setCoolness(total);
        usersConnectRepository.save(usersConnect);
        updateNickname(member, total);
        return total;
    }

    /**
     * Scores one chat message and applies the points.
     * @param event Discord message event, bot messages are ignored
     */
    public void handleMessage(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        Member member = event.getMember();
        if (member == null) {
            return;
        }
        UsersConnect usersConnect = usersConnectRepository.findByDiscordUserId(member.getId());
        if (usersConnect == null) {
            return;
        }
        String content = event.getMessage().getContentDisplay();
        int delta = score(content, !event.getMessage().getAttachments().isEmpty(), member.getId());
        if (delta == 0) {
            return;
        }
        int total = Math.min(MAX_COOLNESS, Math.max(0, usersConnect.getCoolness() + delta));
        usersConnect.setCoolness(total);
        usersConnectRepository.save(usersConnect);
        updateNickname(member, total);
    }

    /**
     * Returns the star count for a rating.
     * @param coolness rating value
     * @return stars from 0 to 5
     */
    public int starsFor(int coolness) {
        return Math.min(MAX_STARS, Math.max(0, coolness / COOLNESS_PER_STAR));
    }

    /**
     * Returns the star suffix for a rating, empty when there are no stars.
     * @param coolness rating value
     * @return star characters or an empty string
     */
    public String starsSuffix(int coolness) {
        int stars = starsFor(coolness);
        return stars == 0 ? "" : "★".repeat(stars);
    }

    /**
     * Scores message content with transparent rules.
     * @param content message text
     * @param hasAttachments whether the message has attachments
     * @param discordUserId author id for duplicate detection
     * @return points to add, may be negative
     */
    private int score(String content, boolean hasAttachments, String discordUserId) {
        String previous = lastMessageByUser.put(discordUserId, content);
        if (!content.isBlank() && content.equals(previous)) {
            return -3;
        }
        int delta = 1;
        if (content.length() >= 100) {
            delta += 2;
        }
        if (hasAttachments) {
            delta += 2;
        }
        if (content.contains("```")) {
            delta += 2;
        }
        if (isShouting(content)) {
            delta -= 2;
        }
        return delta;
    }

    /**
     * Checks for long all-caps shouting.
     * @param content message text
     * @return true for ten or more uppercase letters
     */
    private boolean isShouting(String content) {
        long letters = content.chars().filter(Character::isLetter).count();
        return letters >= 10 && content.chars().filter(Character::isLetter).allMatch(Character::isUpperCase);
    }

    /**
     * Rewrites the member nickname with the current star suffix when it changed.
     * @param member guild member
     * @param coolness new rating value
     */
    private void updateNickname(Member member, int coolness) {
        int stars = starsFor(coolness);
        String current = member.getNickname() != null ? member.getNickname() : member.getUser().getName();
        String base = current.replaceAll("\\s*★+\\s*$", "");
        if (base.isBlank()) {
            base = member.getUser().getName();
        }
        String desired = stars == 0 ? base : base + " " + "★".repeat(stars);
        Integer applied = appliedStarsByMember.get(member.getId());
        if (applied != null && applied == stars && desired.equals(current)) {
            return;
        }
        try {
            Guild guild = member.getGuild();
            guild.modifyNickname(member, desired).queue(
                    v -> appliedStarsByMember.put(member.getId(), stars),
                    err -> log.warn("Could not update nickname for {}: {}", member.getId(), err.getMessage()));
        } catch (PermissionException e) {
            log.warn("No permission to update nickname for {}: {}", member.getId(), e.getMessage());
        }
    }
}
