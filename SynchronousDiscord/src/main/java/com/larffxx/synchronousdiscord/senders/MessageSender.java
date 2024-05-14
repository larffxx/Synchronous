package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
@Setter
public class MessageSender extends Sender<JsonNode>{
    private final UsersConnectDAO usersConnectDAO;
    private final ServersConnectDAO serversConnectDAO;
    public MessageSender(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, UsersConnectDAO usersConnectDAO) {
        super(eventReceiver);
        this.serversConnectDAO = serversConnectDAO;
        this.usersConnectDAO = usersConnectDAO;
    }

    @Override
    public void send(JsonNode data) {
        Matcher matcher = Pattern.compile(getUSERNAME_PATTER()).matcher(data.findValue("message").asText());
        if (matcher.find()) {
            for (Member member : getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()).getTextChannelsByName("telegram", true).get(0).getMembers()) {
                if (usersConnectDAO.existsByDiscordId(member.getUser().getId())) {
                    if (matcher.group().replace("@", "").equals(usersConnectDAO.getByDiscordId(member.getId()).getTelegramName())) {
                        String formattedMSG = data.findValue("message").asText().replace(matcher.group(), member.getUser().getAsMention());
                        getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()).getTextChannelsByName("telegram", true).get(0)
                                .sendMessage(data.findValue("name").asText() + ": " + formattedMSG).queue();
                    }
                }
            }
        }else {
            getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild()).getTextChannelsByName("telegram", true).get(0)
                    .sendMessage((data.findValue("name").asText() + ": " + data.findValue("message").asText())).queue();
        }
    }

    @Override
    public String getSender() {
        return "message";
    }
}
