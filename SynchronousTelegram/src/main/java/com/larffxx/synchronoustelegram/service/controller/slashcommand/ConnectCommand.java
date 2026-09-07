package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.dto.ServersConnectDTO;
import com.larffxx.synchronoustelegram.domain.exception.command.NoOptionsProvidedException;
import com.larffxx.synchronoustelegram.domain.exception.command.TooManyOptionsException;
import com.larffxx.synchronoustelegram.domain.mapper.Mapper;
import com.larffxx.synchronoustelegram.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Service;

/**
 * Slash command that connects a Telegram chat to a Discord channel.
 */
@Service
public class ConnectCommand implements Command {
    /**
     * Service for sending text replies to Telegram chats.
     */
    private final TextMessageService textMessageService;
    /**
     * Repository for server connection records.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a connect command with its dependencies.
     *
     * @param textMessageService service for sending text replies
     * @param serversConnectRepository repository for server connections
     */
    public ConnectCommand(TextMessageService textMessageService, ServersConnectRepository serversConnectRepository) {
        this.textMessageService = textMessageService;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Connects the chat from the context to the given Discord channel.
     *
     * @param commandContext parsed command invocation context
     * @throws NoOptionsProvidedException if no option is provided
     * @throws TooManyOptionsException if more than one option is provided
     */
    @Override
    public void execute(CommandContext commandContext) {
        Mapper<ServersConnect, ServersConnectDTO> mapper = new ServersConnectMapper();
        if (commandContext.options().isEmpty()) {
            throw new NoOptionsProvidedException(InfExcMessage.NO_OPTIONS_FOR_COMMAND_EXCEPTION);
        }
        if(commandContext.options().size() > 1){
            throw new TooManyOptionsException(InfExcMessage.TOO_MANY_OPTIONS_FOR_COMMAND_EXCEPTION);
        }
        Long chatID = commandContext.chatId();

        if (!serversConnectRepository.existsByTelegramChannel(String.valueOf(commandContext.chatId()))) {
            ServersConnectDTO serversConnectDTO = new ServersConnectDTO(commandContext.options().get(0), String.valueOf(chatID));
            serversConnectRepository.save(mapper.toEntity(serversConnectDTO));

            textMessageService.send(chatID, CommandConstant.SUCCESSFULLY_CONNECTED);
        } else {
            textMessageService.send(chatID, CommandConstant.UNSUCCESSFULLY_CONNECTED);
        }
    }


    /**
     * Returns the command name.
     *
     * @return command name without leading slash
     */
    @Override
    public String getCommand() {
        return "connect";
    }
}
