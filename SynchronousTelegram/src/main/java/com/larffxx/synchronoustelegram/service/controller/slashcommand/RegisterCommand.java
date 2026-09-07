package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.dto.UsersConnectDTO;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.domain.exception.command.NoOptionsProvidedException;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.exception.command.TooManyOptionsException;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.mapper.Mapper;
import com.larffxx.synchronoustelegram.domain.mapper.UsersConnectMapper;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Service;

/**
 * Slash command that registers a Telegram user against a Discord name.
 */
@Service
public class RegisterCommand implements Command {
    /**
     * Service for sending text replies to Telegram chats.
     */
    private final TextMessageService textMessageService;
    /**
     * Repository for user connection records.
     */
    private final UsersConnectRepository usersConnectRepository;
    /**
     * Repository for server connection records.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a register command with its dependencies.
     *
     * @param textMessageService service for sending text replies
     * @param usersConnectRepository repository for user connections
     * @param serversConnectRepository repository for server connections
     */
    public RegisterCommand(TextMessageService textMessageService, UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository) {
        this.textMessageService = textMessageService;
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Registers the invoking user with the Discord name from the options.
     *
     * @param commandContext parsed command invocation context
     * @throws NoOptionsProvidedException if no Discord name is provided
     * @throws TooManyOptionsException if more than two options are provided
     */
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
        ServersConnect serversConnect = serversConnectRepository.findByTelegramChannel(commandContext.chatId().toString());
        if (serversConnect == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }
        UsersConnectDTO usersConnectDTO = new UsersConnectDTO(serversConnect.getId(),
                discordName, telegramName);
        UsersConnect usersConnect = mapper.toEntity(usersConnectDTO);
        usersConnectRepository.save(usersConnect);

        textMessageService.send(chatID, CommandConstant.USER_SUCCESSFULLY_REGISTERED);
    }

    /**
     * Returns the command name.
     *
     * @return command name without leading slash
     */
    public String getCommand() {
        return "register";
    }
}
