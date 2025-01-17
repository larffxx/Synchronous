package com.larffxx.synchronousdiscord.controller.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.senders.EmbedSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class ProfileCommand implements Command {
    private final EmbedSender embedSender;
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;


    public ProfileCommand(ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO, EmbedSender embedSender) {
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.embedSender = embedSender;
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
            embedSender.send(eb);
            event.reply(CommandInfMessages.PROFILE_SUCCESS_MESSAGE).queue();
        } else {
            event.reply(CommandInfMessages.PROFILE_UNSUCCESSFUL_MESSAGE).queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByTelegramName(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText());
        if (profileDAO.existsByUsersConnect(profileDAO.getUsersConnectDAO().getByTelegramName(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText()))) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText())
                    .setTitle(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText())
                    .setDescription(profileDAO.getProfile(user.getDiscordName()).getDescription())
                    .setImage(profileDAO.getProfile(user.getDiscordName()).getPhotoUrl())
                    .setUrl(profileDAO.getProfile(user.getDiscordName()).getSocialUrl());
            embedSender.send(eb);
        } else {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText())
                    .setDescription(CommandInfMessages.PROFILE_UNSUCCESSFUL_MESSAGE);
            embedSender.send(eb);
        }
    }

    @Override
    public String getCommand() {
        return "profile";
    }


}
