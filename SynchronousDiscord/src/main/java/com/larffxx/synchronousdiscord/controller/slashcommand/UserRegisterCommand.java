package com.larffxx.synchronousdiscord.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterCommand implements Command {
    private final UsersConnectDAO usersConnectDAO;
    private final ServersConnectDAO serversConnectDAO;
    private final EventReceiver eventReceiver;


    public UserRegisterCommand(UsersConnectDAO usersConnectDAO, ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver) {
        this.usersConnectDAO = usersConnectDAO;
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getUser().isBot()) {
            UsersConnect usersConnect = new UsersConnect(event.getInteraction().getUser().getName(),
                    event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString(),
                    event.getInteraction().getUser().getId(), serversConnectDAO.getByDiscordGuild(event.getGuild().getId()));
            try {
                if (usersConnectDAO.getByDiscordName(event.getInteraction().getUser().getName()).getDiscordName().equals(usersConnect.getDiscordName())) {
                    event.reply(CommandConstants.USER_REGISTER_UNSUCCESSFUL_MESSAGE).queue();
                }
            } catch (NullPointerException e) {
                usersConnectDAO.saveData(usersConnect);
                event.reply(CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
            }
        }
    }

    @Override
    public void execute(JsonNode data) {
        Guild guild = eventReceiver.getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(CommandConstants.GUILD_ID_FROM_TELEGRAM).asText()).getDiscordGuild());
        TextChannel telegramChannel = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true).get(0);

        telegramChannel.sendMessage(data.findValue(CommandConstants.NAME_FROM_TELEGRAM).asText() + CommandConstants.USER_REGISTER_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "register";
    }


}
