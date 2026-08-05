package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.dto.UsersConnectDTO;
import com.larffxx.synchronoustelegram.domain.exception.command.NoOptionsProvidedException;
import com.larffxx.synchronoustelegram.domain.exception.command.TooManyOptionsException;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.mapper.Mapper;
import com.larffxx.synchronoustelegram.domain.mapper.UsersConnectMapper;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Service;

@Service
public class RegisterCommand implements Command {
    private final TextMessageService textMessageService;
    private final UsersConnectRepository usersConnectRepository;
    private final ServersConnectRepository serversConnectRepository;

    public RegisterCommand(TextMessageService textMessageService, UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository) {
        this.textMessageService = textMessageService;
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(CommandContext commandContext) {
        if (commandContext.options().isEmpty()) {
            throw new NoOptionsProvidedException(InfExcMessage.NO_OPTIONS_FOR_COMMAND_EXCEPTION);
        }

        if (commandContext.options().size() > 2) {
            throw new TooManyOptionsException(InfExcMessage.TOO_MANY_OPTIONS_FOR_COMMAND_EXCEPTION);
        }

        Long chatID = commandContext.chatId();
        String discordName = commandContext.options().get(0);
        String telegramName = commandContext.executedBy();

        if(usersConnectRepository.existsByTelegramName(telegramName)){
            textMessageService.send(chatID, CommandConstant.USER_UNSUCCESSFULLY_REGISTERED);
            return;
        }

        Mapper<UsersConnect, UsersConnectDTO> mapper = new UsersConnectMapper();
        UsersConnectDTO usersConnectDTO = new UsersConnectDTO(serversConnectRepository.findByTelegramChannel(commandContext.chatId().toString()).getId(),
                discordName, telegramName);
        UsersConnect usersConnect = mapper.toEntity(usersConnectDTO);
        usersConnectRepository.save(usersConnect);

        textMessageService.send(chatID, CommandConstant.USER_SUCCESSFULLY_REGISTERED);
    }

    public String getCommand() {
        return "register";
    }
}
