package com.larffxx.synchronousweb.controllers;

import com.larffxx.synchronousweb.chat.config.ChatAccessFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Exchanges a configured chat token for a short-lived HTTP-only browser cookie.
 */
@RestController
@RequestMapping("/api/messages")
public class ChatAccessController {
    private final String configuredToken;

    public ChatAccessController(@Value("${chat.auth.token:}") String configuredToken) {
        this.configuredToken = configuredToken == null ? "" : configuredToken;
    }

    @PostMapping("/session")
    public ResponseEntity<Void> createSession(
            @RequestHeader(value = "X-Chat-Access-Token", required = false) String token,
            HttpServletResponse response) {
        if (configuredToken.isBlank() || !configuredToken.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String encodedToken = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(token.getBytes(StandardCharsets.UTF_8));
        response.addHeader("Set-Cookie", ChatAccessFilter.ACCESS_COOKIE + "=" + encodedToken
                + "; Max-Age=1800; Path=/; HttpOnly; SameSite=Strict");
        return ResponseEntity.noContent().build();
    }
}
