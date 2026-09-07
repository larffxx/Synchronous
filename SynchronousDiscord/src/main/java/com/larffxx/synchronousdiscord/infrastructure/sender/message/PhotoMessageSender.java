package com.larffxx.synchronousdiscord.infrastructure.sender.message;

import com.larffxx.synchronousdiscord.domain.constant.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Photo Message Sender class.
 */
@Component
@Getter
@Setter
public class PhotoMessageSender implements Sender<TelegramMessageContext> {
    /**
     * The event context.
     */
    private final EventContext eventContext;
    /**
     * The servers connect repository.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a new PhotoMessageSender.
     * @param eventContext the event context.
     * @param serversConnectRepository the servers connect repository.
     */
    public PhotoMessageSender(EventContext eventContext, ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventContext = eventContext;
    }

    /**
     * Sends telegram message context.
     * @param telegramMessageContext the telegram message context.
     */
    @Override
    public void send(TelegramMessageContext telegramMessageContext) {
        sendFileMessage(telegramMessageContext, telegramMessageContext.textChannel());
    }

    /**
     * Sends file message.
     * @param telegramMessageContext the telegram message context.
     * @param textChannel the text channel.
     */
    private void sendFileMessage(TelegramMessageContext telegramMessageContext, TextChannel textChannel) {
        File inputFile = new File(telegramMessageContext.file());

        String message = "";
        if (!telegramMessageContext.message().equals("null")) {
            message = telegramMessageContext.message();
        }

        textChannel
                .sendMessage(telegramMessageContext.username() + ": " + message)
                .addFiles(FileUpload.fromData(inputFile, SendersConstants.PHOTO_NAME))
                .setEmbeds(new EmbedBuilder().setImage(SendersConstants.PHOTO_ATTACHMENT).build()).queue();
    }

    /**
     * Returns sender.
     * @return the resulting string.
     */
    @Override
    public String getSender() {
        return "PHOTO_MESSAGE";
    }
}
