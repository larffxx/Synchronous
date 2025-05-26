package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.GuildProfileRepository;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextMessageSender {
    private final UpdateHandler updateHandler;
    private final ServersConnectRepository serversConnectRepository;
    private final GuildProfileRepository guildProfileRepository;
    private Matcher matcher;

    public TextMessageSender(UpdateHandler updateHandler, ServersConnectRepository serversConnectRepository, GuildProfileRepository guildProfileRepository) {
        this.updateHandler = updateHandler;
        this.serversConnectRepository = serversConnectRepository;
        this.guildProfileRepository = guildProfileRepository;
    }

    public void send(Long id, String text) throws TelegramApiException {
        String telegramChatId = serversConnectRepository.findByDiscordGuild(String.valueOf(id)).getTelegramChannel();
        updateHandler.setChatId(telegramChatId);

        SendMessage sm = SendMessage.builder().chatId(telegramChatId).text(text).build();

        updateHandler.getTelegramClient().execute(sm);
    }

    public void send(DiscordPayload payload) throws TelegramApiException {
        updateHandler.setChatId(serversConnectRepository.findByDiscordGuild(String.valueOf(payload.getGuildID())).getTelegramChannel());
        Long chatID = Long.valueOf(updateHandler.getChatId());
        SendMessage sm;

        setMatcher(payload.getMessage());

        if(matcher.find()) {
            sm = SendMessage.builder().chatId(chatID).text(formatMsg(payload.getMessage())).build();
        }else{
            sm = SendMessage.builder().chatId(chatID).text(payload.getAuthor() + ": " + payload.getMessage()).build();
        }

        updateHandler.getTelegramClient().execute(sm);
    }

    private void setMatcher(String msg){
        String USERNAME_PATTER = "@([a-zA-Z0-9._\\-]{3,})";
        matcher = Pattern.compile(USERNAME_PATTER).matcher(msg);
    }

    private String formatMsg(String msg){
        String discordName = matcher.group().replace("@","");
        return msg.replace(matcher.group(),
                "@" + guildProfileRepository.getByName(discordName).getUsersConnect().getTelegramName());
    }
}