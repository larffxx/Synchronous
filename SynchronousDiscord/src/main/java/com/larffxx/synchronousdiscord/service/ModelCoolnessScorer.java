package com.larffxx.synchronousdiscord.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scores chat messages with OpenCode Zen Big Pickle through the OpenCode CLI
 * by default (the free tier only works inside the OpenCode client), falls back
 * to any OpenAI-style HTTP endpoint in {@code http} mode, and finally to
 * lexicon rules when the model is unreachable.
 */
@Component
public class ModelCoolnessScorer {
    private static final Logger log = LoggerFactory.getLogger(ModelCoolnessScorer.class);
    /**
     * Largest single-message gain.
     */
    private static final int MAX_MESSAGE_DELTA = 8;
    /**
     * Largest single-message loss.
     */
    private static final int MIN_MESSAGE_DELTA = -5;
    /**
     * Longest message fragment sent to the model.
     */
    private static final int MAX_PROMPT_CONTENT = 500;
    /**
     * Longest whole context block sent to the model.
     */
    private static final int MAX_PROMPT_CONTEXT = 1200;
    /**
     * Dota ranks from worst to best, kept as a fallback scale when the model
     * answers with a rank word instead of a 1-10 number.
     */
    private static final String[][] RANK_NAMES = {
            {"herald", "геральд", "херальд", "хералд"},
            {"guardian", "страж", "гардиан", "гвардиан"},
            {"crusader", "крестоносец", "крусадер"},
            {"archon", "архонт", "архон"},
            {"legend", "легенда", "ледженд"},
            {"ancient", "властелин", "древний", "эншент", "эйншент"},
            {"divine", "божество", "дивайн"},
            {"immortal", "титан", "иммортал", "имморт"},
    };
    /**
     * Words of gratitude and support.
     */
    private static final Pattern KIND_WORDS = words("спасибо", "благодарю", "пожалуйста", "молодец", "поздравляю",
            "thanks", "thank you", "congrats");
    /**
     * Positive and fun words.
     */
    private static final Pattern POSITIVE_WORDS = words("круто", "класс", "супер", "отлично", "прикольно", "смешно",
            "лол", "хаха", "интересно", "полезно", "огонь", "топ", "имба", "нравится", "люблю",
            "cool", "nice", "lol", "haha", "funny", "great");
    /**
     * Insults and hard profanity, minus three points per distinct word.
     */
    private static final Pattern TOXIC_WORDS = words("дурак", "идиот", "тупой", "дебил", "заткнись", "ненавижу",
            "отстой", "урод", "говно", "дерьмо", "херня", "хер",
            "хуй", "хуйня", "хуево", "хуёво", "пиздец", "пизда", "пиздеть",
            "ебать", "ебаный", "ебанутый", "еблан", "блядь", "бля",
            "сука", "мудак", "мудила", "пидор", "гандон", "залупа", "ублюдок", "тварь", "падла",
            "fuck", "shit", "bitch", "idiot", "stupid", "hate", "shut up");
    /**
     * Softened profanity, minus one point per distinct word.
     */
    private static final Pattern MILD_WORDS = words("фигня", "фиг", "нафиг", "пофиг", "хрень", "хрен",
            "блин", "черт", "жопа", "задница", "капец", "пипец", "darn", "crap");

    private final String apiUrl;
    private final String apiModel;
    private final String apiKey;
    private final int timeoutSeconds;
    private final String mode;
    private final String opencodeBin;
    private final String opencodeModel;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    /**
     * Caps parallel CLI runs: each one boots its own server, more than a few
     * at once slow each other down into timeouts.
     */
    private final Semaphore cliPermits = new Semaphore(3);
    /**
     * Last message content per Discord user id, for duplicate detection.
     */
    private final Map<String, String> lastMessageByUser = new ConcurrentHashMap<>();

    /**
     * Creates a scorer with model connection settings.
     * @param apiUrl base URL of the OpenAI-compatible API for {@code http} mode
     * @param apiModel model name for scoring in {@code http} mode
     * @param apiKey API key, may be blank for keyless endpoints
     * @param timeoutSeconds request timeout in seconds
     * @param mode scoring backend: {@code opencode} CLI (default) or {@code http}
     * @param opencodeBin OpenCode CLI binary for {@code opencode} mode
     * @param opencodeModel model selector for {@code opencode} mode
     */
    public ModelCoolnessScorer(
            @Value("${coolness.apiUrl}") String apiUrl,
            @Value("${coolness.apiModel}") String apiModel,
            @Value("${coolness.apiKey:}") String apiKey,
            @Value("${coolness.apiTimeoutSeconds:60}") int timeoutSeconds,
            @Value("${coolness.mode:opencode}") String mode,
            @Value("${coolness.opencodeBin:opencode}") String opencodeBin,
            @Value("${coolness.opencodeModel:opencode/big-pickle}") String opencodeModel) {
        this.apiUrl = apiUrl;
        this.apiModel = apiModel;
        this.apiKey = apiKey;
        this.timeoutSeconds = timeoutSeconds;
        this.mode = mode;
        this.opencodeBin = opencodeBin;
        this.opencodeModel = opencodeModel;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
        if ("http".equalsIgnoreCase(mode) && (apiKey == null || apiKey.isBlank())) {
            log.warn("No coolness API key configured, model scoring will fall back to lexicon rules.");
        }
    }

    /**
     * Score with a short model-written verdict.
     * @param delta points to add, may be negative
     * @param comment model comment about the message
     */
    public record Score(int delta, String comment) {
    }

    /**
     * Scores one message with the model, falling back to lexicon rules on failure.
     * @param content message text
     * @param hasAttachments whether the message has attachments
     * @param discordUserId author id for duplicate detection
     * @param context recent channel messages, oldest first, without the current one
     * @return score with the model comment
     */
    public Score score(String content, boolean hasAttachments, String discordUserId, List<String> context) {
        String previous = lastMessageByUser.put(discordUserId, content);
        if (!content.isBlank() && content.equals(previous)) {
            return new Score(-3, "Я это уже слышал. Дежавю слабака. Оценка: 1/10. Совет: не повторяйся.");
        }
        long startedAt = System.nanoTime();
        try {
            if ("http".equalsIgnoreCase(mode)) {
                return callModel(content, hasAttachments, context);
            }
            return callOpenCodeCli(content, hasAttachments, context);
        } catch (Exception e) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
            log.warn("Model scoring failed after {}ms, using lexicon fallback: {}",
                    elapsedMs, e.getMessage());
            return lexiconScore(content, hasAttachments);
        }
    }

    /**
     * Builds the scoring prompt for one message with recent chat context.
     * @param content message text
     * @param hasAttachments whether the message has attachments
     * @param context recent channel messages, oldest first, without the current one
     * @return prompt asking for a score and a short comment
     */
    private String buildPrompt(String content, boolean hasAttachments, List<String> context) {
        String fragment = content.length() > MAX_PROMPT_CONTENT
                ? content.substring(0, MAX_PROMPT_CONTENT)
                : content;
        String contextBlock = buildContextBlock(context);
        return "Ты — Гуль из чата. Дединсайд-дотер, фанат Tokyo Ghoul. "
                + "Дерзкий и высокомерный снаружи, но по-своему справедливый: удачную шутку, дельную мысль, "
                + "помощь и живое участие замечаешь и ценишь, хоть и сквозь зубы. "
                + "Оцениваешь КАЖДОЕ сообщение по крутости. Отвечаешь цинично, с дота-сленгом. "
                + "Не выходишь из роли. "
                + (hasAttachments ? "К сообщению приложены файлы. " : "")
                + contextBlock
                + "Оценивай сообщение В КОНТЕКСТЕ переписки: короткая реакция на смешное или меткий ответ "
                + "в споре — это тоже круто, а не пустяк. "
                + "Шкала щедрая: 1-2 — только токсичность, спам и нытьё; 3-4 — скука и пустота; "
                + "5 — нейтрально; 6-7 — обычное живое сообщение (так бывает чаще всего); "
                + "8-9 — реально круто; 10 — легенда чата. "
                + "Ответь строго в формате: ОЦЕНКА | РАЗБОР. Оценка — целое число от 1 до 10. "
                + "Разбор — пара циничных предложений свысока плюс надменный совет в конце. "
                + "Без рассуждений и вступлений — только одна строка в заданном формате. "
                + "Пример: 7 | Очередной смертный купил клавиатуру. Думает, железо решит. "
                + "Маску надень, может, поможет. Хотя вряд ли. "
                + "Текст: \"" + fragment + "\"";
    }

    /**
     * Formats recent chat messages as a prompt context block.
     * @param context recent channel messages, oldest first, may be null or empty
     * @return context lines for the prompt, or a no-context note
     */
    private String buildContextBlock(List<String> context) {
        if (context == null || context.isEmpty()) {
            return "Предыдущих сообщений в этом чате нет — судишь вслепую. ";
        }
        List<String> lines = new ArrayList<>();
        int budget = MAX_PROMPT_CONTEXT;
        for (int i = context.size() - 1; i >= 0 && budget > 0; i--) {
            String line = context.get(i);
            if (line == null || line.isBlank()) {
                continue;
            }
            String clean = line.replaceAll("\\s+", " ").trim();
            if (clean.length() > budget) {
                clean = clean.substring(0, budget);
            }
            budget -= clean.length();
            lines.add(0, clean);
        }
        if (lines.isEmpty()) {
            return "Предыдущих сообщений в этом чате нет — судишь вслепую. ";
        }
        return "Контекст чата (свежее — в конце): " + String.join(" // ", lines) + ". ";
    }

    /**
     * Extracts the 1-10 score and the review from model output,
     * mapping the score onto the internal -5..8 delta scale.
     * Takes the first line with a numeric score; a legacy Dota rank is
     * still accepted as a fallback. Tolerates markdown bold, numbered
     * lists and an "Оценка:" prefix, so trailing model chatter with a
     * pipe character cannot hijack the verdict.
     * @param output raw model output
     * @return score with the model review, review falls back to a tier phrase
     * @throws IllegalStateException when the output has no score,
     *         with a truncated snippet of the output for diagnostics
     */
    private Score parseAnswer(String output) {
        String text = output == null ? "" : output;
        String unknownHead = null;
        Integer numericDelta = null;
        String numericComment = "";
        Integer rankDelta = null;
        String rankComment = "";
        for (String rawLine : text.split("\\R")) {
            String line = rawLine.trim()
                    .replaceAll("^[\\s*#>\\-–—]+", "")
                    .replaceAll("^\\d+[.)\\]:]+\\s*", "")
                    .replaceAll("\\*", "")
                    .replaceFirst("(?iu)^(оценка|score|ранг|rank|вердикт|verdict)\\s*[:：]\\s*", "")
                    .trim();
            if (line.isEmpty()) {
                continue;
            }
            int pipe = line.indexOf('|');
            if (pipe < 0) {
                if (line.matches("-?\\d+") && numericDelta == null) {
                    numericDelta = tenPointToDelta(Integer.parseInt(line));
                    numericComment = "";
                }
                continue;
            }
            String token = line.substring(0, pipe).trim().replaceAll("[\"'«»]+", "").trim();
            if (token.contains(" ")) {
                token = token.substring(0, token.indexOf(' '));
            }
            token = token.replaceAll("[^\\p{L}0-9-]+", "");
            String comment = line.substring(pipe + 1).trim();
            if (token.isEmpty()) {
                continue;
            }
            if (token.matches("-?\\d+")) {
                if (numericDelta == null) {
                    numericDelta = tenPointToDelta(Integer.parseInt(token));
                    numericComment = comment;
                }
                continue;
            }
            int rankIdx = rankIndex(token);
            if (rankIdx >= 0) {
                if (rankDelta == null) {
                    rankDelta = Math.round(rankIdx * 13f / (RANK_NAMES.length - 1)) - 5;
                    rankComment = comment;
                }
            } else if (unknownHead == null) {
                unknownHead = token;
            }
        }
        Integer delta = numericDelta != null ? numericDelta : rankDelta;
        String comment = numericDelta != null ? numericComment : rankComment;
        if (delta == null && text.strip().matches("-?\\d+")) {
            delta = tenPointToDelta(Integer.parseInt(text.strip()));
            comment = "";
        }
        if (delta != null) {
            int clamped = Math.min(MAX_MESSAGE_DELTA, Math.max(MIN_MESSAGE_DELTA, delta));
            String clean = cleanComment(comment);
            return new Score(clamped, clean.isBlank() ? defaultComment(clamped) : clean);
        }
        if (unknownHead != null) {
            throw new IllegalStateException("No score in model answer (" + unknownHead + "): " + truncate(text));
        }
        throw new IllegalStateException("No score in model answer: " + truncate(text));
    }

    /**
     * Returns the rank position for an English or Russian rank name,
     * including common transliterations the model may produce.
     * @param rank rank word from the model answer
     * @return position from 0 (Herald) to 7 (Immortal), or -1 when unknown
     */
    private int rankIndex(String rank) {
        for (int i = 0; i < RANK_NAMES.length; i++) {
            for (String name : RANK_NAMES[i]) {
                if (name.equalsIgnoreCase(rank)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Maps a 1-10 score onto the internal -5..8 delta scale.
     * @param tenPoint score between 1 and 10
     * @return delta between -5 and 8
     */
    private int tenPointToDelta(int tenPoint) {
        int clamped = Math.min(10, Math.max(1, tenPoint));
        return Math.round((clamped - 1) * 13f / 9 - 5);
    }

    /**
     * Cleans the model comment: single line, no wrapping quotes, capped length.
     * @param raw text after the score number
     * @return cleaned comment, may be empty
     */
    private String cleanComment(String raw) {
        if (raw == null) {
            return "";
        }
        String text = raw.replaceFirst("^[\\s|:;\\-—]+", "").replaceAll("\\s+", " ").trim();
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1).trim();
        }
        if (text.length() > 400) {
            text = text.substring(0, 400).trim() + "…";
        }
        return text;
    }

    /**
     * Returns a fallback comment for a score tier.
     * @param delta score value
     * @return short verdict phrase
     */
    private String defaultComment(int delta) {
        if (delta >= 5) {
            return "Ладно, почти не стыдно. Оценка: 9/10. Совет: не зазнавайся, смертный.";
        }
        if (delta > 0) {
            return "Терпимо. Для низшей ступени сойдёт. Оценка: 7/10. Совет: старайся лучше, стая смотрит.";
        }
        if (delta == 0) {
            return "Пыль. Ни вкуса, ни запаха. Оценка: 5/10. Совет: го некст.";
        }
        if (delta >= -2) {
            return "Жалкое зрелище. Даже гули такое не едят. Оценка: 3/10. Совет: скройся и подумай.";
        }
        return "Мусор. Маску снимаю только чтобы зевнуть. Оценка: 1/10. Совет: исчезни.";
    }

    /**
     * Asks Big Pickle for a score through the OpenCode CLI.
     * The free Zen tier only works inside the OpenCode client, so the bot
     * shells out to {@code opencode run} exactly like an OpenCode session.
     * @param content message text
     * @param hasAttachments whether the message has attachments
     * @param context recent channel messages, oldest first, without the current one
     * @return model score with the model comment
     * @throws Exception when the CLI fails, times out, or answers with no number
     */
    private Score callOpenCodeCli(String content, boolean hasAttachments, List<String> context) throws Exception {
        cliPermits.acquire();
        try {
            ProcessBuilder builder = new ProcessBuilder(opencodeBin, "run", "--model", opencodeModel, buildPrompt(content, hasAttachments, context))
                    .redirectErrorStream(true)
                    .redirectInput(new File("/dev/null"));
            long startedAt = System.nanoTime();
            Process process = builder.start();
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
            if (!finished) {
                process.destroyForcibly();
                throw new IllegalStateException("OpenCode CLI timed out after " + elapsedMs + "ms");
            }
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (process.exitValue() != 0) {
                throw new IllegalStateException("OpenCode CLI exit " + process.exitValue() + ": " + truncate(output));
            }
            Score result = parseAnswer(output);
            log.debug("OpenCode CLI scored in {}ms", elapsedMs);
            return result;
        } finally {
            cliPermits.release();
        }
    }

    /**
     * Shortens CLI output for error messages.
     * @param output raw CLI output
     * @return first 200 characters of the output
     */
    private static String truncate(String output) {
        if (output == null) {
            return "";
        }
        String singleLine = output.replaceAll("\\s+", " ").trim();
        return singleLine.length() > 200 ? singleLine.substring(0, 200) : singleLine;
    }

    /**
     * Asks the model for a 1-10 score with a short review.
     * @param content message text
     * @param hasAttachments whether the message has attachments
     * @param context recent channel messages, oldest first, without the current one
     * @return model score with the model comment
     * @throws Exception when the request fails or the answer has no number
     */
    private Score callModel(String content, boolean hasAttachments, List<String> context) throws Exception {
        String prompt = buildPrompt(content, hasAttachments, context);

        ObjectNode message = objectMapper.createObjectNode();
        message.put("role", "user");
        message.put("content", prompt);
        ArrayNode messages = objectMapper.createArrayNode();
        messages.add(message);
        ObjectNode request = objectMapper.createObjectNode();
        request.put("model", apiModel);
        request.put("temperature", 0);
        request.put("max_tokens", 250);
        request.set("messages", messages);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/chat/completions"))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .header("Content-Type", "application/json")
                .header("HTTP-Referer", "https://hermes-agent.nousresearch.com")
                .header("X-Title", "Synchronous")
                .header("User-Agent", "SynchronousDiscord/1.0")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)));
        if (apiKey != null && !apiKey.isBlank()) {
            builder.header("Authorization", "Bearer " + apiKey);
        }

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("Model API status " + response.statusCode());
        }
        JsonNode answer = objectMapper.readTree(response.body())
                .path("choices").path(0).path("message").path("content");
        return parseAnswer(answer.asText(""));
    }

    /**
     * Scores message content with transparent lexicon rules.
     * @param content message text
     * @param hasAttachments whether the message has attachments
     * @return points to add with a tier fallback comment, may be negative
     */
    private Score lexiconScore(String content, boolean hasAttachments) {
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
        delta += 2 * countDistinct(KIND_WORDS, content);
        delta += countDistinct(POSITIVE_WORDS, content);
        if (content.contains("?")) {
            delta += 1;
        }
        delta -= 3 * countDistinct(TOXIC_WORDS, content);
        delta -= countDistinct(MILD_WORDS, content);
        if (isShouting(content)) {
            delta -= 2;
        }
        delta = Math.min(MAX_MESSAGE_DELTA, Math.max(MIN_MESSAGE_DELTA, delta));
        return new Score(delta, defaultComment(delta));
    }

    /**
     * Builds a case-insensitive whole-word pattern for the given words.
     * @param words words to match
     * @return compiled pattern
     */
    private static Pattern words(String... words) {
        StringBuilder alternation = new StringBuilder();
        for (String word : words) {
            if (alternation.length() > 0) {
                alternation.append('|');
            }
            alternation.append(Pattern.quote(word));
        }
        return Pattern.compile("(?iu)(?<![\\p{L}])(" + alternation + ")(?![\\p{L}])");
    }

    /**
     * Counts distinct lexicon words in the text, repetitions add nothing.
     * @param pattern lexicon pattern
     * @param content message text
     * @return number of distinct matched words
     */
    private static int countDistinct(Pattern pattern, String content) {
        Matcher matcher = pattern.matcher(content);
        Set<String> seen = new HashSet<>();
        while (matcher.find()) {
            seen.add(matcher.group().toLowerCase(Locale.ROOT));
        }
        return seen.size();
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
}
