package com.larffxx.synchronousdiscord.application.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.domain.service.DiscordContextResolveService;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterCommand implements Command {
    private final UsersConnectRepository usersConnectRepository;
    private final ServersConnectRepository serversConnectRepository;
    private final DiscordContextResolveService discordContextResolveService;

    public UserRegisterCommand(UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository, DiscordContextResolveService discordContextResolveService) {
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
        this.discordContextResolveService = discordContextResolveService;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getUser().isBot()) {
            UsersConnect usersConnect = new UsersConnect(event.getInteraction().getUser().getName(),
                    event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString(),
                    event.getInteraction().getUser().getId(), serversConnectRepository.getConnectByDiscordGuild(event.getGuild().getId()));
            try {
                if (usersConnectRepository.findByDiscordName(event.getInteraction().getUser().getName()).getDiscordName().equals(usersConnect.getDiscordName())) {
                    event.reply(CommandConstants.USER_REGISTER_UNSUCCESSFUL_MESSAGE).queue();
                }
            } catch (NullPointerException e) {
                usersConnectRepository.save(usersConnect);
                event.reply(CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
            }
        }
    }

    @Override
    public void execute(JsonNode data) {
        DiscordContext context = discordContextResolveService.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());

        context.textChannel().sendMessage(data.findValue(CommandConstants.NAME_FROM_TELEGRAM).asText() + CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "register";
    }


}
