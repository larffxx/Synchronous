package com.larffxx.synchronousweb.controllers;

import com.larffxx.synchronousweb.chat.api.ChatMessageResponse;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import com.larffxx.synchronousweb.chat.service.ChatMessageService;
import com.larffxx.synchronousweb.chat.service.ChatMessageStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.Map;
import java.util.Locale;

/**
 * API used by the unified chat page.
 */
@RestController
@RequestMapping("/api/messages")
public class ChatMessageController {
    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final int MAX_PAGE_SIZE = 100;

    private final ChatMessageService messageService;
    private final ChatMessageStream stream;

    public ChatMessageController(ChatMessageService messageService, ChatMessageStream stream) {
        this.messageService = messageService;
        this.stream = stream;
    }

    @GetMapping
    public Page<ChatMessageResponse> messages(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String conversation,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        ChatPlatform parsedPlatform = parsePlatform(platform);
        PageRequest pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Order.desc("sentAt"), Sort.Order.desc("id")));
        return messageService.findMessages(parsedPlatform, conversation, from, to, pageable);
    }

    @GetMapping(path = "/stream", produces = "text/event-stream")
    public SseEmitter stream() {
        return stream.subscribe();
    }

    private ChatPlatform parsePlatform(String platform) {
        if (platform == null || platform.isBlank() || "ALL".equalsIgnoreCase(platform)) {
            return null;
        }
        return ChatPlatform.valueOf(platform.trim().toUpperCase(Locale.ROOT));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidRequest(IllegalArgumentException exception) {
        return Map.of("error", "Invalid message query");
    }
}
