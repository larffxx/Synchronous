package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class PlayCommand implements Command {
    private final ServersConnectRepository serversConnectRepository;
    private final TextMessageService textMessageService;

    public PlayCommand(ServersConnectRepository serversConnectRepository, TextMessageService textMessageService) {
        this.serversConnectRepository = serversConnectRepository;
        this.textMessageService = textMessageService;
    }

    @Override
    public void execute(CommandContext commandContext){
        textMessageService.send(commandContext.chatId(), CommandConstant.MUSIC_ADDED);
    }

    @Override
    public String getCommand() {
        return "play";
    }
}
