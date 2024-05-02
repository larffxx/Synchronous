package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfileCommand extends Command {
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final CommandListener commandListener;
    public ProfileCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, CommandListener commandListener, UsersConnectDAO usersConnectDAO, ProfileDAO profileDAO) {
        super(kafkaTemplate, eventReceiver);
        this.commandListener = commandListener;
        this.usersConnectDAO = usersConnectDAO;
        this.profileDAO = profileDAO;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(profileDAO.existsByUsersConnect(profileDAO.getUsersConnectDAO().getByDiscordName(event.getInteraction().getUser().getName()))){
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(event.getUser().getName())
                    .setTitle(event.getUser().getName() + ": profile")
                    .setDescription(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getDescription())
                    .setImage(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getPhotoUrl())
                    .setUrl(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getSocialUrl());
            commandListener.getEmbedSender().send(eb);
            event.reply("Your profile").queue();
        }else {
            event.reply("Create profile with /create command").queue();
        }
    }

    @Override
    public void execute(String commandName) {

    }

    @Override
    public String getCommand() {
        return "profile";
    }
}
