package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class EditProfileCommand implements Command{
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final EventReceiver eventReceiver;


    public EditProfileCommand(ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO, EventReceiver eventReceiver) {
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        profileDAO.updateProfile(t.getOption(CommandInfMessages.DESCRIPTION_OPTION).getAsString(),
                t.getOption(CommandInfMessages.PHOTO_OPTION).getAsAttachment().getUrl(),
                t.getOption(CommandInfMessages.URL_OPTION).getAsString(),
                usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()));
        t.reply(CommandInfMessages.EDIT_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByDiscordName(data.get(CommandInfMessages.NAME_FROM_TELEGRAM).asText());
        Guild guild = eventReceiver.getJda().getGuildById(user.getServersConnect().getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelById(data.get(CommandInfMessages.TELEGRAM_CHANNEL).asText());

        textChannel.sendMessage(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText() + CommandInfMessages.EDIT_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "edit";
    }


}
