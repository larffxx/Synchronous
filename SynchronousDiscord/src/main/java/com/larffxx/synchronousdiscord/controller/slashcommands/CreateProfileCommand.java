package com.larffxx.synchronousdiscord.controller.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.model.Profile;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.repo.UsersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CreateProfileCommand implements Command {
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final UsersConnectRepository usersConnectRepository;
    private final EventReceiver eventReceiver;


    public CreateProfileCommand(ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO, UsersConnectRepository usersConnectRepository, EventReceiver eventReceiver) {
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.usersConnectRepository = usersConnectRepository;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!profileDAO.existsByUsersConnect(usersConnectDAO.getByDiscordName(event.getInteraction().getUser().getName()))) {
            Profile profile = new Profile(event.getOption(CommandInfMessages.DESCRIPTION_OPTION).getAsString(),
                    event.getOption(CommandInfMessages.PHOTO_OPTION).getAsAttachment().getUrl(),
                    event.getOption(CommandInfMessages.URL_OPTION).getAsString(),
                    usersConnectRepository.getReferenceById(usersConnectDAO.getByDiscordName(event.getInteraction().getUser().getName()).getId()));
            profileDAO.saveModel(profile);
            event.getHook().editOriginal(CommandInfMessages.CREATE_PROFILE_SUCCESS_MESSAGE).queue();
        } else {
            event.getHook().editOriginal(CommandInfMessages.CREATE_PROFILE_UNSUCCESSFUL_MESSAGE).queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByDiscordName(data.findValue(CommandInfMessages.NAME_FROM_TELEGRAM).asText());
        Guild guild = eventReceiver.getJda().getGuildById(user.getServersConnect().getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(CommandInfMessages.DISCORD_TEXT_CHANNEL, true).get(0);

        textChannel.sendMessage(data.findValue("name").asText() + CommandInfMessages.CREATE_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "create";
    }

}
