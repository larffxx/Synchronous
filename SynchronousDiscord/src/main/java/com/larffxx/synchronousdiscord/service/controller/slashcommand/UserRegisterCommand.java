package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.dto.UsersConnectDTO;
import com.larffxx.synchronousdiscord.domain.mapper.Mapper;
import com.larffxx.synchronousdiscord.domain.mapper.UsersConnectMapper;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterCommand implements Command {
    private final UsersConnectRepository usersConnectRepository;
    private final ServersConnectRepository serversConnectRepository;

    public UserRegisterCommand(UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository) {
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
    }


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

    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        telegramCommandContext.textChannel().sendMessage(telegramCommandContext.telegramUsername() + CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "register";
    }


}
