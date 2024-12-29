package com.larffxx.synchronoustelegram.sender;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Component
public class DiscordMessageSender {
    private final ServersConnectDAO serversConnectDAO;
    private final UpdateHolder updateHolder;
    private final GuildProfileDAO guildProfileDAO;
    private final TextMessageSender textMessageSender;
    private Matcher matcher;

    public DiscordMessageSender(UpdateHolder updateHolder, GuildProfileDAO guildProfileDAO, TextMessageSender textMessageSender, ServersConnectDAO serversConnectDAO) {
        this.updateHolder = updateHolder;
        this.guildProfileDAO = guildProfileDAO;
        this.textMessageSender = textMessageSender;
        this.serversConnectDAO = serversConnectDAO;
    }

    public void sendTextMessageToTelegramChannel(JsonNode data) throws TelegramApiException {
        String guildId = data.findValue("guildId").asText();
        String message = data.findValue("message").asText();
        updateHolder.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(guildId).getTelegramChannel());

        setMatcher(message);
        if(matcher.find()){
            textMessageSender.send(Long.valueOf(updateHolder.getChatId()), formatMsg(message));
        }else {
            textMessageSender.send(Long.valueOf(updateHolder.getChatId()), message);
        }
    }

    private void setMatcher(String msg){
        String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
        matcher = Pattern.compile(USERNAME_PATTER).matcher(msg);
    }

    private String formatMsg(String msg){
        String discordName = matcher.group().replace("@","");
        return msg.replace(matcher.group(),
                "@" + guildProfileDAO.getByName(discordName).getUsersConnect().getTelegramName());
    }

}
