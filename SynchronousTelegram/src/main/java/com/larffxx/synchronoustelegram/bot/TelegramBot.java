package com.larffxx.synchronoustelegram.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.commands.SendText;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.dao.UsersConnectDAO;
import com.larffxx.synchronoustelegram.listeners.CommandListener;
import com.larffxx.synchronoustelegram.listeners.KafkaListeners;
import com.larffxx.synchronoustelegram.preprocessors.ButtonPreProcessor;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
@Setter
@NoArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${name}")
    private String botUsername;
    @Value("${token}")
    private String botToken;
    private ServersConnectDAO serversConnectDAO;
    private CommandListener commandListener;
    private UpdateReceiver updateReceiver;
    private CommandPreProcessor commandPreProcessor;
    private ButtonPreProcessor buttonPreProcessor;
    private KafkaListeners kafkaListener;
    private SendText sendText;
    private UsersConnectDAO usersConnectDAO;
    private GuildProfileDAO guildProfileDAO;

    @Autowired
    public TelegramBot(CommandListener commandListener, UpdateReceiver updateReceiver, CommandPreProcessor commandPreProcessor, ButtonPreProcessor buttonPreProcessor, KafkaListeners kafkaListener, SendText sendText, ServersConnectDAO serversConnectDAO, UsersConnectDAO usersConnectDAO, GuildProfileDAO guildProfileDAO) {
        this.commandListener = commandListener;
        this.updateReceiver = updateReceiver;
        this.commandPreProcessor = commandPreProcessor;
        this.buttonPreProcessor = buttonPreProcessor;
        this.kafkaListener = kafkaListener;
        this.sendText = sendText;
        this.serversConnectDAO = serversConnectDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.commandListener.saveToUpdateReceiver(update);
        Command command;
        if (update.getMessage().hasText() && !update.getMessage().getFrom().getIsBot()) {
            if (update.getMessage().getText().startsWith("/")) {
                String[] s = update.getMessage().getText().split(" ");
                command = commandPreProcessor.getCommand(s[0]);
                if (command != null) {
                    try {
                        execute((BotApiMethod) command.execute(updateReceiver));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    kafkaListener.sendKafkaMessage(update, s[0]);
                }
            } else {
                kafkaListener.sendKafkaMessage(update);
            }
        }

        if (update.getMessage().hasPhoto() && !update.getMessage().getFrom().getIsBot()) {
            try {
                File file = downloadFile(update);
                kafkaListener.sendKafkaMessage(update, file);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    @KafkaListener(
            topics = {"${dTopic}"},
            groupId = "${groupId}"
    )
    public void listener(@Payload String user) {
        try {
            JsonNode data = (new ObjectMapper()).readTree(user);
            String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
            Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(data.findValue("message").asText());
            updateReceiver.setChatId(serversConnectDAO.getChatByDiscordGuild(data.findValue("chatId").asText()).getTelegramChannel());
            if (matcher.find()) {
                String formattedMSG = data.findValue("message").asText().replace(matcher.group(),
                        "@" + guildProfileDAO.getByName(matcher.group().replace("@", "")).getUsersConnect().getTelegramName());
                execute(sendText.execute(Long.valueOf(updateReceiver.getChatId()), data.findValue("name").asText() + ": " + formattedMSG));
            } else {
                execute(sendText.execute(Long.valueOf(updateReceiver.getChatId()),  data.findValue("name").asText() + ": " + data.findValue("message").asText()));
            }
        } catch (TelegramApiException | JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private File downloadFile(Update update) throws TelegramApiException {
        GetFile getFile = new GetFile();
        getFile.setFileId((update.getMessage().getPhoto().get(2)).getFileId());
        File file = downloadFile(execute(getFile).getFilePath());
        return file;
    }
}

