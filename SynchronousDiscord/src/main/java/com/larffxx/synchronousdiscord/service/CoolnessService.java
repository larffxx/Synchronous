package com.larffxx.synchronousdiscord.service;

import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tracks user coolness ratings, mirrors them as stars in Discord nicknames,
 * and scores chat messages with a free local model asynchronously.
 */
@Component
public class CoolnessService {
    private static final Logger log = LoggerFactory.getLogger(CoolnessService.class);
    /**
     * Maximum coolness rating.
     */
    public static final int MAX_COOLNESS = 100;
    /**
     * Minimum coolness rating, losers sink below zero.
     */
    public static final int MIN_COOLNESS = -100;
    /**
     * Coolness points per nickname star.
     */
    public static final int COOLNESS_PER_STAR = 20;
    /**
     * Maximum nickname stars.
     */
    public static final int MAX_STARS = 5;
    /**
     * Recent messages per channel kept as scoring context.
     */
    private static final int CONTEXT_SIZE = 6;
    /**
     * Longest context message fragment kept for scoring.
     */
    private static final int MAX_CONTEXT_CONTENT = 200;

    private final UsersConnectRepository usersConnectRepository;
    /**
     * Scores message content with a free model.
     */
    private final ModelCoolnessScorer scorer;
    /**
     * Background pool for model scoring, so chat handling never blocks.
     * Sized above single-model latency so a burst of messages scores in parallel
     * instead of queueing behind one slow call.
     */
    private final ExecutorService scoringPool = Executors.newFixedThreadPool(8, task -> {
        Thread thread = new Thread(task, "coolness-scorer");
        thread.setDaemon(true);
        return thread;
    });
    /**
     * Last applied star count per Discord user id, to avoid redundant nickname updates.
     */
    private final Map<String, Integer> appliedStarsByMember = new ConcurrentHashMap<>();
    /**
     * Rolling message context per channel id, newest last, for model scoring.
     */
    private final Map<String, Deque<String>> recentByChannel = new ConcurrentHashMap<>();

    /**
     * Creates a coolness service.
     * @param usersConnectRepository repository for user connections
     * @param scorer model based message scorer
     */
    public CoolnessService(UsersConnectRepository usersConnectRepository, ModelCoolnessScorer scorer) {
        this.usersConnectRepository = usersConnectRepository;
        this.scorer = scorer;
    }

    /**
     * Returns the coolness of a member, creating a record for newcomers.
     * @param member guild member
     * @return coolness rating
     */
    public int getCoolness(Member member) {
        return findOrCreate(member.getId(), member.getUser().getName()).getCoolness();
    }

    /**
     * Returns the coolness of the user linked to a Telegram name, creating a record for newcomers.
     * @param telegramName Telegram name
     * @return coolness rating
     */
    public int getCoolnessByTelegramName(String telegramName) {
        UsersConnect usersConnect = usersConnectRepository.findByTelegramName(telegramName);
        if (usersConnect == null) {
            usersConnect = new UsersConnect();
            usersConnect.setTelegramName(telegramName);
            usersConnect.setCoolness(0);
            usersConnect = usersConnectRepository.save(usersConnect);
        }
        return usersConnect.getCoolness();
    }

    /**
     * Adds coolness to a member and refreshes the nickname stars.
     * @param member guild member to rate
     * @param delta points to add, may be negative
     * @return new total rating
     */
    public int addCoolness(Member member, int delta) {
        UsersConnect usersConnect = findOrCreate(member.getId(), member.getUser().getName());
        int total = Math.min(MAX_COOLNESS, Math.max(MIN_COOLNESS, usersConnect.getCoolness() + delta));
        usersConnect.setCoolness(total);
        usersConnectRepository.save(usersConnect);
        updateNickname(member, total);
        return total;
    }

    /**
     * Scores one chat message in the background and applies the points.
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
        String userId = member.getId();
        String userName = event.getAuthor().getName();
        String displayName = member.getEffectiveName();
        String content = event.getMessage().getContentDisplay();
        boolean hasAttachments = !event.getMessage().getAttachments().isEmpty();
        String messageId = event.getMessageId();
        var channel = event.getChannel();
        channel.sendTyping().queue(null, err -> log.debug("Typing indicator failed: {}", err.getMessage()));
        List<String> context = rememberAndContext(channel.getId(), displayName, content);
        scoringPool.execute(() -> {
            try {
                ModelCoolnessScorer.Score result = scorer.score(content, hasAttachments, userId, context);
                int delta = result.delta();
                UsersConnect usersConnect = findOrCreate(userId, userName);
                int total = Math.min(MAX_COOLNESS, Math.max(MIN_COOLNESS, usersConnect.getCoolness() + delta));
                usersConnect.setCoolness(total);
                usersConnectRepository.save(usersConnect);
                updateNickname(member, total);
                String opinion = formatOpinion(displayName, delta, total, result.comment());
                try {
                    channel.sendMessage(opinion)
                            .setMessageReference(messageId)
                            .queue(
                                    v -> log.info("Coolness opinion sent for {}: delta={}, total={}", userId, delta, total),
                                    err -> {
                                        log.warn("Reply with coolness opinion failed, sending plain: {}", err.getMessage());
                                        channel.sendMessage(opinion).queue(
                                                ok -> log.info("Coolness opinion sent plain for {}", userId),
                                                e2 -> log.warn("Could not send coolness opinion: {}", e2.getMessage()));
                                    });
                } catch (Exception e) {
                    log.warn("Could not send coolness opinion: {}", e.getMessage());
                }
            } catch (Exception e) {
                log.warn("Coolness scoring failed: {}", e.getMessage());
            }
        });
    }

    /**
     * Stops the background scoring pool.
     */
    @PreDestroy
    public void shutdown() {
        scoringPool.shutdown();
        try {
            scoringPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Snapshots recent channel messages for scoring, then remembers the current one.
     * @param channelId Discord channel id
     * @param displayName author display name
     * @param content message text
     * @return previous messages, oldest first, without the current one
     */
    private List<String> rememberAndContext(String channelId, String displayName, String content) {
        Deque<String> recent = recentByChannel.computeIfAbsent(channelId, id -> new ArrayDeque<>());
        List<String> context;
        String fragment = content.length() > MAX_CONTEXT_CONTENT
                ? content.substring(0, MAX_CONTEXT_CONTENT) + "…"
                : content;
        synchronized (recent) {
            context = new ArrayList<>(recent);
            recent.addLast(displayName + ": " + fragment);
            while (recent.size() > CONTEXT_SIZE) {
                recent.pollFirst();
            }
        }
        return context;
    }

    /**
     * Finds the user record or creates a coolness-only one.
     * @param discordUserId Discord user id
     * @param discordName Discord username
     * @return existing or new user connection
     */
    private UsersConnect findOrCreate(String discordUserId, String discordName) {
        UsersConnect usersConnect = usersConnectRepository.findByDiscordUserId(discordUserId);
        if (usersConnect == null) {
            usersConnect = new UsersConnect();
            usersConnect.setDiscordUserId(discordUserId);
            usersConnect.setDiscordName(discordName);
            usersConnect.setCoolness(0);
            usersConnect = usersConnectRepository.save(usersConnect);
        }
        return usersConnect;
    }

    /**
     * Formats the bot opinion about one message for the chat.
     * @param name author display name
     * @param delta points given for the message, may be zero
     * @param total new rating value
     * @param comment model comment about the message
     * @return chat message with the verdict
     */
    private String formatOpinion(String name, int delta, int total, String comment) {
        String stars = starsSuffix(total);
        String suffix = stars.isEmpty() ? "" : " " + stars;
        String deltaText = delta > 0 ? "+" + delta : String.valueOf(delta);
        String verdict;
        if (delta >= 5) {
            verdict = "Достойно";
        } else if (delta > 0) {
            verdict = "Терпимо";
        } else if (delta == 0) {
            verdict = "Пыль";
        } else if (delta >= -2) {
            verdict = "Жалко";
        } else {
            verdict = "Мусор";
        }
        return String.format("%s, %s! Крутость %s (%s), всего: %d%s%s",
                name, verdict, deltaText, contentWord(delta), total, suffix,
                comment == null || comment.isBlank() ? "" : "\n💬 " + comment.trim());
    }

    /**
     * Returns the Russian word form for coolness points.
     * @param delta points value
     * @return word form after the number
     */
    private String contentWord(int delta) {
        int value = Math.abs(delta) % 100;
        int digit = value % 10;
        if (value > 10 && value < 20) {
            return "очков";
        }
        if (digit == 1) {
            return "очко";
        }
        if (digit >= 2 && digit <= 4) {
            return "очка";
        }
        return "очков";
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
     * Rewrites the member nickname with the current star suffix when it changed.
     * The guild owner is skipped: Discord forbids changing the owner's nickname.
     * @param member guild member
     * @param coolness new rating value
     */
    private void updateNickname(Member member, int coolness) {
        if (member.isOwner()) {
            return;
        }
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
