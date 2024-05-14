package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.kafka.core.KafkaTemplate;
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
        if (!data.findValue("message").asText().equals("null") && !data.findValue("file").asText().equals("null")) {
            getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()).getTextChannelsByName("telegram", true).get(0)
                    .sendMessage(data.findValue("name").asText() + ": " + data.findValue("message").asText())
                    .addFiles(FileUpload.fromData(new File(data.findPath("file").asText()), "photo.png"))
                    .setEmbeds(new EmbedBuilder().setImage("attachment://photo.png").build()).queue();
        } else {
            getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()).getTextChannelsByName("telegram", true).get(0)
                    .sendMessage(data.findValue("name").asText() + ": ")
                    .addFiles(FileUpload.fromData(new File(data.findPath("file").asText()), "photo.png"))
                    .setEmbeds(new EmbedBuilder().setImage("attachment://photo.png").build()).queue();
        }
    }

    @Override
    public String getSender() {
        return "fileMessage";
    }
}
