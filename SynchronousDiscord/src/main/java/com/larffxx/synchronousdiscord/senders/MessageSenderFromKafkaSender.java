package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageSenderFromKafkaSender extends Sender<JsonNode> {
    private final ServersConnectDAO serversConnectDAO;
    private final CommandPreProcessor commandPreProcessor;
    private final UsersConnectDAO usersConnectDAO;
    private final GuildProfileDAO guildProfileDAO;
    private final String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";

    public MessageSenderFromKafkaSender(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, CommandPreProcessor commandPreProcessor, ServersConnectDAO serversConnectDAO, UsersConnectDAO usersConnectDAO, GuildProfileDAO guildProfileDAO) {
        super(kafkaTemplate, eventReceiver);
        this.commandPreProcessor = commandPreProcessor;
        this.serversConnectDAO = serversConnectDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
    }

    @Override
    public void send(JsonNode data) {
        if (data.findValue("command").asText().startsWith("/")) {
            StringBuilder builder = new StringBuilder(data.findValue("command").asText());
            builder.deleteCharAt(0);
            Command command = commandPreProcessor.getCommand(String.valueOf(builder));
            command.execute(data.findValue("message").asText());
        }
        if (data.findValue("file").asText().equals("null")) {
            Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(data.findValue("message").asText());
            if (matcher.find()) {
                for (Member member : getEventReceiver().getJda().getGuildById(serversConnectDAO.getGuildByTelegramChat(data.findValue("chatId").asText())).getTextChannelsByName("telegram", true).get(0).getMembers()) {
                    if(usersConnectDAO.existsByDiscordId(member.getUser().getId())){
                        if(matcher.group().replace("@", "").equals(usersConnectDAO.getByDiscordId(member.getId()).getTelegramName())) {
                            String formattedMSG = data.findValue("message").asText().replace(matcher.group(), member.getUser().getAsMention());
                            getEventReceiver().getJda().getGuildById(serversConnectDAO.getGuildByTelegramChat(data.findValue("chatId").asText())).getTextChannelsByName("telegram", true).get(0)
                                    .sendMessage(data.findValue("name").asText() + ": " + formattedMSG).queue();
                        }
                    }
                }
            } else {
                getEventReceiver().getJda().getGuildById(serversConnectDAO.getGuildByTelegramChat(data.findValue("chatId").asText())).getTextChannelsByName("telegram", true).get(0)
                        .sendMessage((data.findValue("name").asText() + ": " + data.findValue("message").asText())).queue();
            }
        } else {
            if (!data.findValue("message").asText().equals("null") && !data.findValue("file").asText().equals("null")) {
                getEventReceiver().getJda().getGuildById(serversConnectDAO.getGuildByTelegramChat(data.findValue("chatId").asText())).getTextChannelsByName("telegram", true).get(0)
                        .sendMessage(data.findValue("name").asText() + ": " + data.findValue("message").asText())
                        .addFiles(FileUpload.fromData(new File(data.findPath("file").asText()), "photo.png"))
                        .setEmbeds(new EmbedBuilder().setImage("attachment://photo.png").build()).queue();
            } else {
                getEventReceiver().getJda().getGuildById(serversConnectDAO.getGuildByTelegramChat(data.findValue("chatId").asText())).getTextChannelsByName("telegram", true).get(0)
                        .sendMessage(data.findValue("name").asText() + ": ")
                        .addFiles(FileUpload.fromData(new File(data.findPath("file").asText()), "photo.png"))
                        .setEmbeds(new EmbedBuilder().setImage("attachment://photo.png").build()).queue();
            }
        }
    }


}


