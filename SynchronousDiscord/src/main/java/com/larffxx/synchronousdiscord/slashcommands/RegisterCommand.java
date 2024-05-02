package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RegisterCommand extends Command {
    private final UsersConnectDAO usersConnectDAO;
    private final ServersConnectDAO serversConnectDAO;

    public RegisterCommand(KafkaTemplate kafkaTemplate, EventReceiver eventReceiver, UsersConnectDAO usersConnectDAO, ServersConnectDAO serversConnectDAO) {
        super(kafkaTemplate, eventReceiver);
        this.usersConnectDAO = usersConnectDAO;
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getUser().isBot()) {
            UsersConnect usersConnect = new UsersConnect(event.getInteraction().getUser().getName(), event.getOption("telegram").getAsString(),event.getInteraction().getUser().getId(), serversConnectDAO.getByDiscordGuild(event.getGuild().getId()));
            try {
                if (usersConnectDAO.getByDiscordName(event.getInteraction().getUser().getName()).getDiscordName().equals(usersConnect.getDiscordName())) {
                    event.reply("You have been registered before").queue();
                }
            } catch (NullPointerException e) {
                usersConnectDAO.saveData(usersConnect);
                event.reply("registered").queue();
            }
        }
    }

    @Override
    public void execute(String commandName) {
        getCommand();
    }

    @Override
    public String getCommand() {
        return "register";
    }
}
