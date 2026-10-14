package com.larffxx.synchronousweb.controllers;

import com.larffxx.synchronousweb.chat.api.ChatMessageResponse;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import com.larffxx.synchronousweb.chat.service.ChatMessageService;
import com.larffxx.synchronousweb.chat.service.ChatMessageStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatMessageController.class)
class ChatMessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService messageService;

    @MockBean
    private ChatMessageStream stream;

    @Test
    void returnsFilteredMessagesAsJson() throws Exception {
        ChatMessageResponse message = new ChatMessageResponse(
                1L, "discord-1", ChatPlatform.DISCORD, "channel-1", "general",
                "alice", "Hello", Instant.parse("2026-10-14T10:15:30Z"), 0, List.of());
        when(messageService.findMessages(eq(ChatPlatform.DISCORD), eq("general"),
                isNull(), isNull(), any())).thenReturn(new PageImpl<>(List.of(message)));

        mockMvc.perform(get("/api/messages")
                        .param("platform", "DISCORD")
                        .param("conversation", "general"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].platform").value("DISCORD"))
                .andExpect(jsonPath("$.content[0].conversationName").value("general"))
                .andExpect(jsonPath("$.content[0].content").value("Hello"));
    }
}
