package com.larffxx.synchronousweb.controllers;

import com.larffxx.synchronousweb.chat.service.ChatAttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Streams stored images through the authenticated web API.
 */
@RestController
@RequestMapping("/api/messages")
public class ChatAttachmentController {
    private final ChatAttachmentService attachmentService;

    public ChatAttachmentController(ChatAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/{messageId}/attachments/{attachmentId}")
    public ResponseEntity<Resource> image(@PathVariable Long messageId,
                                           @PathVariable Long attachmentId) {
        return attachmentService.find(messageId, attachmentId)
                .map(download -> {
                    MediaType mediaType = MediaType.parseMediaType(download.attachment().getContentType());
                    String disposition = ContentDisposition.inline()
                            .filename(download.attachment().getOriginalName(), StandardCharsets.UTF_8)
                            .build()
                            .toString();
                    return ResponseEntity.ok()
                            .contentType(mediaType)
                            .contentLength(download.attachment().getSize())
                            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePrivate())
                            .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                            .body(download.resource());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
