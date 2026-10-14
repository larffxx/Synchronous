package com.larffxx.synchronousweb.controllers;

import com.larffxx.synchronousweb.chat.domain.ChatAttachment;
import com.larffxx.synchronousweb.chat.domain.ChatMessage;
import com.larffxx.synchronousweb.chat.domain.ChatPlatform;
import com.larffxx.synchronousweb.chat.domain.StoredChatAttachment;
import com.larffxx.synchronousweb.chat.service.ChatAttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatAttachmentController.class)
class ChatAttachmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatAttachmentService attachmentService;

    @Test
    void streamsImageInline() throws Exception {
        ChatMessage message = new ChatMessage(
                ChatPlatform.TELEGRAM, "telegram-1", "42", "Team", "alice", "Look",
                Instant.now(), Instant.now(), 1);
        ChatAttachment attachment = new ChatAttachment(message,
                new StoredChatAttachment("image.jpg", "photo.jpg", "image/jpeg", 3));
        ByteArrayResource resource = new ByteArrayResource(new byte[]{1, 2, 3});
        when(attachmentService.find(eq(42L), eq(7L)))
                .thenReturn(Optional.of(new ChatAttachmentService.ChatAttachmentDownload(attachment, resource)));

        mockMvc.perform(get("/api/messages/42/attachments/7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("inline")))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }
}
