package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersInfMessages;
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
public class FileMessageSender extends Sender<JsonNode>{
    private final ServersConnectDAO serversConnectDAO;
    public FileMessageSender(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public void send(JsonNode data) {
        Guild guild = getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(SendersInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(SendersInfMessages.TEXT_CHANNEL_IN_DISCORD, true).get(0);

        sendFileMessage(data, textChannel);
    }

    private void sendFileMessage(JsonNode data, TextChannel textChannel){
        if (!data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText().equals("null") && !data.findValue(SendersInfMessages.FILE_FROM_TELEGRAM).asText().equals("null")) {
            textChannel
                    .sendMessage(data.findValue(SendersInfMessages.NAME_IN_TELEGRAM).asText() + ": " + data.findValue(SendersInfMessages.MESSAGE_FROM_TELEGRAM).asText())
                    .addFiles(FileUpload.fromData(new File(data.findPath(SendersInfMessages.FILE_FROM_TELEGRAM).asText()), SendersInfMessages.PHOTO_NAME))
                    .setEmbeds(new EmbedBuilder().setImage(SendersInfMessages.PHOTO_ATTACHMENT).build()).queue();
        } else {
            textChannel
                    .sendMessage(data.findValue(SendersInfMessages.NAME_IN_TELEGRAM).asText() + ": ")
                    .addFiles(FileUpload.fromData(new File(data.findPath(SendersInfMessages.FILE_FROM_TELEGRAM).asText()), SendersInfMessages.PHOTO_NAME))
                    .setEmbeds(new EmbedBuilder().setImage(SendersInfMessages.PHOTO_ATTACHMENT).build()).queue();
        }
    }

    @Override
    public String getSender() {
        return "fileMessageSender";
    }
}
