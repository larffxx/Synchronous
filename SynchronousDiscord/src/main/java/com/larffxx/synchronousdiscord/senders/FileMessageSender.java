package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
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
public class FileMessageSender implements Sender<JsonNode>{
    private final EventReceiver eventReceiver;
    private final ServersConnectDAO serversConnectDAO;


    public FileMessageSender(ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver) {
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void send(JsonNode data) {
        Guild guild = eventReceiver.getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(SendersConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersConstants.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        sendFileMessage(data, textChannel);
    }

    private void sendFileMessage(JsonNode data, TextChannel textChannel){
        if (!data.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText().equals("null") && !data.findValue(SendersConstants.FILE_FROM_TELEGRAM).asText().equals("null")) {
            textChannel
                    .sendMessage(data.findValue(SendersConstants.NAME_IN_TELEGRAM).asText() + ": " + data.findValue(SendersConstants.MESSAGE_FROM_TELEGRAM).asText())
                    .addFiles(FileUpload.fromData(new File(data.findPath(SendersConstants.FILE_FROM_TELEGRAM).asText()), SendersConstants.PHOTO_NAME))
                    .setEmbeds(new EmbedBuilder().setImage(SendersConstants.PHOTO_ATTACHMENT).build()).queue();
        } else {
            textChannel
                    .sendMessage(data.findValue(SendersConstants.NAME_IN_TELEGRAM).asText() + ": ")
                    .addFiles(FileUpload.fromData(new File(data.findPath(SendersConstants.FILE_FROM_TELEGRAM).asText()), SendersConstants.PHOTO_NAME))
                    .setEmbeds(new EmbedBuilder().setImage(SendersConstants.PHOTO_ATTACHMENT).build()).queue();
        }
    }

    @Override
    public String getSender() {
        return "fileMessage";
    }
}
