package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.dto.UsersConnectDTO;
import com.larffxx.synchronousdiscord.domain.mapper.Mapper;
import com.larffxx.synchronousdiscord.domain.mapper.UsersConnectMapper;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.TelegramSlashInteractionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

/**
 * Registers a Discord user with their Telegram name.
 */
@Service
public class UserRegisterCommand implements Command {
    /**
     * Repository for Discord to Telegram user connections.
     */
    private final UsersConnectRepository usersConnectRepository;
    /**
     * Repository for Discord to Telegram server connections.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a user registration command.
     *
     * @param usersConnectRepository repository for user connections
     * @param serversConnectRepository repository for server connections
     */
    public UserRegisterCommand(UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
    }


    /**
     * Registers the Discord user from a slash command interaction.
     *
     * @param event Discord slash command interaction event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Mapper<UsersConnect, UsersConnectDTO> mapper = new UsersConnectMapper();
        if (!event.getUser().isBot()) {
            ServersConnect serversConnect = serversConnectRepository.getConnectByDiscordGuild(event.getGuild().getId());
            if (serversConnect == null) {
                throw new CommandException(InfExcMessages.NO_CONNECTION_BETWEEN_SERVERS);
            }
            String username = event.getInteraction().getUser().getName();
            String userId = event.getInteraction().getUser().getId();
            UsersConnect existing = usersConnectRepository.findByDiscordUserId(userId);
            if (existing == null) {
                existing = usersConnectRepository.findByDiscordName(username);
            }
            if (existing != null && existing.getTelegramName() != null) {
                event.getHook().editOriginal(CommandConstants.USER_REGISTER_UNSUCCESSFUL_MESSAGE).queue();
                return;
            }
            UsersConnectDTO dto = new UsersConnectDTO(username,
                    event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString(),
                    userId,
                    serversConnect.getId()
                    );
            UsersConnect entity = mapper.toEntity(dto);
            if (existing != null) {
                entity.setId(existing.getId());
            }
            usersConnectRepository.save(entity);
            event.getHook().editOriginal(CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
        }
    }

    /**
     * Links a Telegram user to their Discord member record.
     *
     * @param telegramCommandContext Telegram command context with the Telegram username
     */
    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        Mapper<UsersConnect, UsersConnectDTO> mapper = new UsersConnectMapper();
        UsersConnect usersConnect = usersConnectRepository.findByTelegramName(telegramCommandContext.telegramUsername());
        if (usersConnect == null) {
            throw new TelegramSlashInteractionException(InfExcMessages.NO_REGISTERED_USERS);
        }
        UsersConnectDTO usersConnectDTO = mapper.toDTO(usersConnect);
        Member discordUser = telegramCommandContext.guild()
                .getMembersByName(usersConnectDTO.getDiscordName(), false)
                .stream()
                .findFirst().orElseThrow(() -> new TelegramSlashInteractionException(InfExcMessages.NO_REGISTERED_USERS));
        usersConnectRepository.updateDiscordUserIdByTelegramName(discordUser.getId(), telegramCommandContext.telegramUsername());
        telegramCommandContext.textChannel().sendMessage(telegramCommandContext.telegramUsername() + " " +CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
    }

    /**
     * Returns the register command name.
     *
     * @return register command name
     */
    @Override
    public String getCommand() {
        return "register";
    }


}
