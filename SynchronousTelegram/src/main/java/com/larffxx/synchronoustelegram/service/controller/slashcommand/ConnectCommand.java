package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.dto.ServersConnectDTO;
import com.larffxx.synchronoustelegram.domain.exception.command.TooManyOptionsException;
import com.larffxx.synchronoustelegram.domain.mapper.Mapper;
import com.larffxx.synchronoustelegram.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import org.springframework.stereotype.Service;

@Service
public class ConnectCommand implements Command {
    private final TextMessageService textMessageService;
    private final ServersConnectRepository serversConnectRepository;

    public ConnectCommand(TextMessageService textMessageService, ServersConnectRepository serversConnectRepository) {
        this.textMessageService = textMessageService;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(CommandContext commandContext) {
        Mapper<ServersConnect, ServersConnectDTO> mapper = new ServersConnectMapper();
        if(commandContext.options().size() > 1){
            throw new TooManyOptionsException(InfExcMessage.TOO_MANY_OPTIONS_FOR_COMMAND_EXCEPTION);
        }
        Long chatID = commandContext.chatId();

        if (!serversConnectRepository.existsByTelegramChannel(String.valueOf(commandContext.chatId()))) {
            ServersConnectDTO serversConnectDTO = new ServersConnectDTO(commandContext.options().get(0), String.valueOf(chatID));
            serversConnectRepository.save(mapper.toEntity(serversConnectDTO));

            textMessageService.send(chatID, "connected");
        } else {
            textMessageService.send(chatID, "connected before");
        }
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
