package com.larffxx.synchronousweb.chat.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Optional boundary for the read-only chat API.
 * Set CHAT_ACCESS_TOKEN in production to require a token; leaving it empty keeps local development open.
 */
@Component
public class ChatAccessFilter extends OncePerRequestFilter {
    public static final String ACCESS_COOKIE = "synchronous_chat_access";
    private static final String ACCESS_HEADER = "X-Chat-Access-Token";

    private final String configuredToken;

    public ChatAccessFilter(@Value("${chat.auth.token:}") String configuredToken) {
        this.configuredToken = configuredToken == null ? "" : configuredToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!isProtectedPath(request) || configuredToken.isBlank() || isAuthorized(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Chat access token required");
    }

    private boolean isProtectedPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith(request.getContextPath() + "/api/messages")
                && !path.equals(request.getContextPath() + "/api/messages/session");
    }

    private boolean isAuthorized(HttpServletRequest request) {
        String headerToken = request.getHeader(ACCESS_HEADER);
        if (matches(headerToken)) {
            return true;
        }
        if (request.getCookies() == null) {
            return false;
        }
        for (Cookie cookie : request.getCookies()) {
            if (ACCESS_COOKIE.equals(cookie.getName())) {
                try {
                    String token = new String(Base64.getUrlDecoder().decode(cookie.getValue()), StandardCharsets.UTF_8);
                    return matches(token);
                } catch (IllegalArgumentException ignored) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean matches(String token) {
        if (token == null) {
            return false;
        }
        return MessageDigest.isEqual(
                configuredToken.getBytes(StandardCharsets.UTF_8),
                token.getBytes(StandardCharsets.UTF_8));
    }
}
