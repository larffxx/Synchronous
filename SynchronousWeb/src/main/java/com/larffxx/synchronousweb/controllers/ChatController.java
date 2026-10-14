package com.larffxx.synchronousweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves the unified read-only chat page.
 */
@Controller
public class ChatController {
    @GetMapping("/chat")
    public String chat() {
        return "pages/chat";
    }
}
