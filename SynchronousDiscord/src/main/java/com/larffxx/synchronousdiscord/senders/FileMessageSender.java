package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
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
        Guild guild = getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName("telegram", true).get(0);

        sendFileMessage(data, textChannel);
    }

    private void sendFileMessage(JsonNode data, TextChannel textChannel){
        if (!data.findValue("message").asText().equals("null") && !data.findValue("file").asText().equals("null")) {
            textChannel
                    .sendMessage(data.findValue("name").asText() + ": " + data.findValue("message").asText())
                    .addFiles(FileUpload.fromData(new File(data.findPath("file").asText()), "photo.png"))
                    .setEmbeds(new EmbedBuilder().setImage("attachment://photo.png").build()).queue();
        } else {
            textChannel
                    .sendMessage(data.findValue("name").asText() + ": ")
                    .addFiles(FileUpload.fromData(new File(data.findPath("file").asText()), "photo.png"))
                    .setEmbeds(new EmbedBuilder().setImage("attachment://photo.png").build()).queue();
        }
    }

    @Override
    public String getSender() {
        return "fileMessageSender";
    }
}
