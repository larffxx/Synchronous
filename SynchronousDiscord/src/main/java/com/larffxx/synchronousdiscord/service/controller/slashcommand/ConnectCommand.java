package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronousdiscord.domain.dto.ServersConnectDTO;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ConnectCommand implements Command{
    private final EventContext eventContext;
    private final ServersConnectRepository serversConnectRepository;
    private final ServersConnectMapper serversConnectMapper;

    public ConnectCommand(EventContext eventContext, ServersConnectRepository serversConnectRepository, ServersConnectMapper serversConnectMapper) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventContext = eventContext;
        this.serversConnectMapper = serversConnectMapper;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        ServersConnectDTO dto = new ServersConnectDTO(
                event.getGuild().getId(),
                event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString()
        );

        ServersConnect serversConnect = serversConnectMapper.toEntity(dto);

        serversConnectRepository.save(serversConnect);
        event.getHook().editOriginal(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        telegramCommandContext.textChannel().sendMessage(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
