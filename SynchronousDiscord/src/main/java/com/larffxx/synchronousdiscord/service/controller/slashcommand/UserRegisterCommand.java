package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.dto.UsersConnectDTO;
import com.larffxx.synchronousdiscord.domain.mapper.Mapper;
import com.larffxx.synchronousdiscord.domain.mapper.UsersConnectMapper;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
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
            UsersConnectDTO dto = new UsersConnectDTO(event.getInteraction().getUser().getName(),
                    event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString(),
                    event.getInteraction().getUser().getId(),
                    serversConnectRepository.getConnectByDiscordGuild(event.getGuild().getId()).getId()
                    );
            try {
                if (usersConnectRepository.findByDiscordName(event.getInteraction().getUser().getName()).getDiscordName().equals(dto.getDiscordName())) {
                    event.getHook().editOriginal(CommandConstants.USER_REGISTER_UNSUCCESSFUL_MESSAGE).queue();
                }
            } catch (NullPointerException e) {
                usersConnectRepository.save(mapper.toEntity(dto));
                event.getHook().editOriginal(CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
            }
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
        UsersConnectDTO usersConnectDTO = mapper.toDTO(usersConnectRepository.findByTelegramName(telegramCommandContext.telegramUsername()));
        Member discordUser = telegramCommandContext.guild()
                .getMembersByName(usersConnectDTO.getDiscordName(), false)
                .stream()
                .findFirst().get();
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
