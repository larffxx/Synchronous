package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class QueueCommand implements Command {
    private final TextMessageService textMessageService;

    public QueueCommand(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @Override
    public void execute(CommandContext commandContext) throws TelegramApiException {
        if(commandContext.response().isEmpty()){
            textMessageService.send(commandContext.chatId(), CommandConstant.QUEUE);
            return;
        }
        List<String> response = commandContext.response().stream()
                .map(s -> s.replaceAll("\\p{Cf}", "").trim())
                .filter(s -> !s.isEmpty())
                .toList();

        textMessageService.send(
                commandContext.chatId(),String.format("%s%n%s", CommandConstant.QUEUE,
                        IntStream.range(0, response.size())
                                .mapToObj(i -> (i+ 1) + ". " + response.get(i)).collect(Collectors.joining("\n")))
            );
    }

    @Override
    public String getCommand() {
        return "queue";
    }
}
