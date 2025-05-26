package com.larffxx.synchronoustelegram.sender;

import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.payload.DiscordPayload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextMessageSender {
    private final UpdateHolder updateHolder;
    private final GuildProfileDAO guildProfileDAO;
    private final ServersConnectDAO serversConnectDAO;
    private Matcher matcher;

    public TextMessageSender(UpdateHolder updateHolder, GuildProfileDAO guildProfileDAO, ServersConnectDAO serversConnectDAO) {
        this.updateHolder = updateHolder;
        this.guildProfileDAO = guildProfileDAO;
        this.serversConnectDAO = serversConnectDAO;
    }

    public void send(Long id, String text) throws TelegramApiException {
        String telegramChatId = serversConnectDAO.getTelegramChatByDiscordGuild(String.valueOf(id));
        updateHolder.setChatId(telegramChatId);

        SendMessage sm = SendMessage.builder().chatId(telegramChatId).text(text).build();

        updateHolder.getTelegramClient().execute(sm);
    }

    public void send(DiscordPayload payload) throws TelegramApiException {
        updateHolder.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(String.valueOf(payload.getGuildID())));
        Long chatID = Long.valueOf(updateHolder.getChatId());
        SendMessage sm;

        setMatcher(payload.getMessage());

        if(matcher.find()) {
            sm = SendMessage.builder().chatId(chatID).text(formatMsg(payload.getMessage())).build();
        }else{
            sm = SendMessage.builder().chatId(chatID).text(payload.getAuthor() + ": " + payload.getMessage()).build();
        }

        updateHolder.getTelegramClient().execute(sm);
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