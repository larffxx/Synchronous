package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

class CoolnessCommandTest {
    private final TextMessageService textMessageService = mock(TextMessageService.class);
    private final UsersConnectRepository repository = mock(UsersConnectRepository.class);
    private final CoolnessCommand command = new CoolnessCommand(textMessageService, repository);

    @Test
    void reportsMissingUserWithoutCreatingPartialRegistration() throws Exception {
        CommandContext context = new CommandContext(123L, "missing", "coolness", List.of(), List.of());
        when(repository.findByTelegramName("missing")).thenReturn(null);

        command.execute(context);

        verify(textMessageService).send(123L, CommandConstant.USER_NOT_REGISTERED);
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(UsersConnect.class));
    }

    @Test
    void showsRegisteredUserCoolness() throws Exception {
        UsersConnect user = new UsersConnect();
        user.setCoolness(40);
        CommandContext context = new CommandContext(123L, "alice", "coolness", List.of(), List.of());
        when(repository.findByTelegramName("alice")).thenReturn(user);

        command.execute(context);

        verify(textMessageService).send(123L, String.format(CommandConstant.COOLNESS_SHOW, "alice", 40) + " ★★");
    }
}
