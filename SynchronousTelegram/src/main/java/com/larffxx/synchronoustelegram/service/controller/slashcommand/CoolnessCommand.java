package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Shows a coolness rating as text.
 */
@Service
public class CoolnessCommand implements Command {
    private final TextMessageService textMessageService;
    private final UsersConnectRepository usersConnectRepository;

    /**
     * Creates a coolness command.
     * @param textMessageService service for sending text replies
     * @param usersConnectRepository repository for user links
     */
    public CoolnessCommand(TextMessageService textMessageService, UsersConnectRepository usersConnectRepository) {
        this.textMessageService = textMessageService;
        this.usersConnectRepository = usersConnectRepository;
    }

    /**
     * Replies with the coolness of the named user or the sender.
     * @param commandContext parsed command invocation context
     */
    @Override
    public void execute(CommandContext commandContext) throws TelegramApiException {
        UsersConnect target;
        if (!commandContext.options().isEmpty()) {
            target = usersConnectRepository.findByDiscordName(commandContext.options().get(0));
        } else {
            target = usersConnectRepository.findByTelegramName(commandContext.executedBy());
        }
        if (target == null) {
            textMessageService.send(commandContext.chatId(), CommandConstant.USER_NOT_REGISTERED);
            return;
        }
        int total = target.getCoolness();
        int stars = Math.min(5, total / 20);
        String suffix = stars == 0 ? "" : " " + "★".repeat(stars);
        textMessageService.send(commandContext.chatId(),
                String.format(CommandConstant.COOLNESS_SHOW, target.getDiscordName(), total) + suffix);
    }

    /**
     * Returns the command name.
     * @return command name without leading slash
     */
    @Override
    public String getCommand() {
        return "coolness";
    }
}
