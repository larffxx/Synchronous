package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import org.springframework.stereotype.Component;

@Component
public class TextMessageService implements MessageService {
    private final UpdateReceiver updateReceiver;
    private final TextMessageSender textMessageSender;
    private final ServersConnectRepository serversConnectRepository;

    public TextMessageService(UpdateReceiver updateReceiver, TextMessageSender textMessageSender, ServersConnectRepository serversConnectRepository) {
        this.updateReceiver = updateReceiver;
        this.textMessageSender = textMessageSender;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void send(DiscordPayload payload) {
        updateReceiver.setChatId(serversConnectRepository.findByDiscordGuild(String.valueOf(payload.getGuildID())).getTelegramChannel());

        textMessageSender.send(payload);
    }

    public void send(Long chatId, String text) {
        textMessageSender.send(chatId, text);
    }

    @Override
    public String getType() {
        return "TEXT_MESSAGE";
    }
}
