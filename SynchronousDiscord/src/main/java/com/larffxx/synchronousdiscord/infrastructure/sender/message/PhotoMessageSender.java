package com.larffxx.synchronousdiscord.infrastructure.sender.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.constants.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Getter
@Setter
public class PhotoMessageSender implements Sender<JsonNode> {
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;

    public PhotoMessageSender(EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void send(JsonNode data) {
        Guild guild = eventReceiver.getJda().getGuildById(serversConnectRepository.getConnectByTelegramChannel(data.findValue(SendersConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersConstants.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        sendFileMessage(data, textChannel);
    }

    private void sendFileMessage(JsonNode data, TextChannel textChannel) {
        File inputFile = new File(data.findValue(SendersConstants.FILE_FROM_TELEGRAM).asText());

        String message = "";
        if (!data.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText().equals("null")) {
            message = data.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText();
        }

        textChannel
                .sendMessage(data.findValue(SendersConstants.NAME_IN_TELEGRAM).asText() + ": " + message)
                .addFiles(FileUpload.fromData(inputFile, SendersConstants.PHOTO_NAME))
                .setEmbeds(new EmbedBuilder().setImage(SendersConstants.PHOTO_ATTACHMENT).build()).queue();
    }

    @Override
    public String getSender() {
        return "PHOTO_MESSAGE";
    }
}
