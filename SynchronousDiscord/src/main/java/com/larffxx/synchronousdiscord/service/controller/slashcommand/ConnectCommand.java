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

/**
 * Connects a Discord guild to a Telegram channel.
 */
@Component
@Getter
@Setter
public class ConnectCommand implements Command{
    /**
     * Shared context holding the current channel.
     */
    private final EventContext eventContext;
    /**
     * Repository for Discord to Telegram server connections.
     */
    private final ServersConnectRepository serversConnectRepository;
    /**
     * Mapper between server connection entities and DTOs.
     */
    private final ServersConnectMapper serversConnectMapper;

    /**
     * Creates a connect command.
     *
     * @param eventContext shared event context
     * @param serversConnectRepository repository for server connections
     * @param serversConnectMapper mapper for server connections
     */
    public ConnectCommand(EventContext eventContext, ServersConnectRepository serversConnectRepository, ServersConnectMapper serversConnectMapper) {
        this.serversConnectRepository = serversConnectRepository;
        this.eventContext = eventContext;
        this.serversConnectMapper = serversConnectMapper;
    }


    /**
     * Saves the Discord guild to Telegram channel connection.
     *
     * @param event Discord slash command interaction event
     */
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

    /**
     * Confirms the existing server connection to the Telegram channel.
     *
     * @param telegramCommandContext Telegram command context
     */
    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        telegramCommandContext.textChannel().sendMessage(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }


    /**
     * Returns the connect command name.
     *
     * @return connect command name
     */
    @Override
    public String getCommand() {
        return "connect";
    }
}
