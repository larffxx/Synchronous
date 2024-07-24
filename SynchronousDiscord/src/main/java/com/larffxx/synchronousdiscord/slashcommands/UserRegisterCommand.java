package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.InfMessages;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterCommand extends Command {
    private final UsersConnectDAO usersConnectDAO;
    private final ServersConnectDAO serversConnectDAO;


    public UserRegisterCommand(EventReceiver eventReceiver, UsersConnectDAO usersConnectDAO, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.usersConnectDAO = usersConnectDAO;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getUser().isBot()) {
            UsersConnect usersConnect = new UsersConnect(event.getInteraction().getUser().getName(),
                    event.getOption(InfMessages.TELEGRAM_CHANNEL).getAsString(),
                    event.getInteraction().getUser().getId(), serversConnectDAO.getByDiscordGuild(event.getGuild().getId()));
            try {
                if (usersConnectDAO.getByDiscordName(event.getInteraction().getUser().getName()).getDiscordName().equals(usersConnect.getDiscordName())) {
                    event.reply(InfMessages.USER_REGISTER_UNSUCCESSFUL_MESSAGE).queue();
                }
            } catch (NullPointerException e) {
                usersConnectDAO.saveData(usersConnect);
                event.reply(InfMessages.USER_REGISTER_SUCCESS_MESSAGE).queue();
            }
        }
    }

    @Override
    public void execute(JsonNode data) {
        Guild guild = getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue(InfMessages.GUILD_ID_FROM_TELEGRAM).asText()).getDiscordGuild());
        TextChannel telegramChannel = guild.getTextChannelsByName(InfMessages.TELEGRAM_CHANNEL, true).get(0);

        telegramChannel.sendMessage(data.findValue(InfMessages.NAME_FROM_TELEGRAM).asText() + InfMessages.USER_REGISTER_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "register";
    }


}
