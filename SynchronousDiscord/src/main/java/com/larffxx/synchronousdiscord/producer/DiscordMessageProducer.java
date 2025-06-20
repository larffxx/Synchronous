package com.larffxx.synchronousdiscord.producer;

import com.larffxx.synchronousdiscord.exception.DownloadAttachmentException;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message.Attachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class DiscordMessageProducer {
    @Value("${dMTopic}")
    private String topic;
    private final KafkaTemplate<String, MessagePayload> messagePayloadKafkaTemplate;

    public DiscordMessageProducer(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.messagePayloadKafkaTemplate = kafkaTemplate;
    }

    public void send(MessageReceivedEvent event) {
        MessagePayload messagePayload = new MessagePayload(event.getAuthor().getName(), event.getMessage().getContentDisplay(), event.getGuild().getIdLong());

        Message<MessagePayload> message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        messagePayloadKafkaTemplate.send(message);
    }

    public void sendWithAttachment(MessageReceivedEvent event){
        List<Attachment> attachments = event.getMessage().getAttachments();

        MessagePayload messagePayload = new MessagePayload(event.getGuild().getIdLong(), event.getAuthor().getName(), event.getMessage().getContentDisplay(), downloadedAttachments(attachments));

        Message<MessagePayload> message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        messagePayloadKafkaTemplate.send(message);
    }

    private List<File> downloadedAttachments(List<Attachment> attachments) {
        List<File> files = new ArrayList<>();

            attachments.forEach(attachment -> {
                attachment.getProxy().downloadToFile(new File("C:/Users/offic/Desktop/tempphotos/" + attachment.getFileName())).thenAccept(path -> {
                }).exceptionally(throwable -> {
                    throw new DownloadAttachmentException(InfExcMessages.DOWNLOAD_ATTACHMENT_EXCEPTION);
                });
                files.add(new File("C:/Users/offic/Desktop/tempphotos/" + attachment.getFileName()));
            });

        return files;
    }
}
