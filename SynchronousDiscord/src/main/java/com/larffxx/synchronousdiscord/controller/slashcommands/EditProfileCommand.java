package com.larffxx.synchronousdiscord.controller.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
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
    public void execute(SlashCommandInteractionEvent event) {
        profileDAO.updateProfile(event.getOption(CommandConstants.DESCRIPTION_OPTION).getAsString(),
                event.getOption(CommandConstants.PHOTO_OPTION).getAsAttachment().getUrl(),
                event.getOption(CommandConstants.URL_OPTION).getAsString(),
                usersConnectDAO.getByDiscordName(event.getInteraction().getUser().getName()));
        event.getHook().editOriginal(CommandConstants.EDIT_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByDiscordName(data.findValue(CommandConstants.NAME_FROM_TELEGRAM).asText());
        Guild guild = eventReceiver.getJda().getGuildById(user.getServersConnect().getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true).get(0);

        textChannel.sendMessage(data.findValue(CommandConstants.NAME_FROM_TELEGRAM).asText() + CommandConstants.EDIT_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "edit";
    }


}
