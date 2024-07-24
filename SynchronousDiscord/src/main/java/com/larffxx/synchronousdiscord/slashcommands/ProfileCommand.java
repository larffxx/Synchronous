package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.InfMessages;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class ProfileCommand extends Command {
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final CommandListener commandListener;

    private final String NAME_FROM_TELEGRAM = "name";
    private final String SUCCESS_MESSAGE = "Your profile";
    private final String UNSUCCESSFUL_MESSAGE = "Create profile with /create command";

    public ProfileCommand(EventReceiver eventReceiver, CommandListener commandListener, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO) {
        super(eventReceiver);
        this.commandListener = commandListener;
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (profileDAO.existsByUsersConnect(profileDAO.getUsersConnectDAO().getByDiscordName(event.getInteraction().getUser().getName()))) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(event.getUser().getName())
                    .setTitle(event.getUser().getName())
                    .setDescription(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getDescription())
                    .setImage(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getPhotoUrl())
                    .setUrl(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getSocialUrl());
            commandListener.getEmbedSender().send(eb);
            event.reply(InfMessages.PROFILE_SUCCESS_MESSAGE).queue();
        } else {
            event.reply(InfMessages.PROFILE_UNSUCCESSFUL_MESSAGE).queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByTelegramName(data.findValue(NAME_FROM_TELEGRAM).asText());
        if (profileDAO.existsByUsersConnect(profileDAO.getUsersConnectDAO().getByTelegramName(data.findValue(NAME_FROM_TELEGRAM).asText()))) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(data.findValue(NAME_FROM_TELEGRAM).asText())
                    .setTitle(data.findValue(NAME_FROM_TELEGRAM).asText())
                    .setDescription(profileDAO.getProfile(user.getDiscordName()).getDescription())
                    .setImage(profileDAO.getProfile(user.getDiscordName()).getPhotoUrl())
                    .setUrl(profileDAO.getProfile(user.getDiscordName()).getSocialUrl());
            commandListener.getEmbedSender().send(eb);
        } else {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(data.findValue(InfMessages.NAME_FROM_TELEGRAM).asText())
                    .setDescription(InfMessages.PROFILE_UNSUCCESSFUL_MESSAGE);
            commandListener.getEmbedSender().send(eb);
        }
    }

    @Override
    public String getCommand() {
        return "profile";
    }


}
