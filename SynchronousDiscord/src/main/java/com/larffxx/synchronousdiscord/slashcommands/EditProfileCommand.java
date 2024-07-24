package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.InfMessages;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

@Component
public class EditProfileCommand extends Command{
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;



    public EditProfileCommand(EventReceiver eventReceiver, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO) {
        super(eventReceiver);
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        profileDAO.updateProfile(t.getOption(InfMessages.DESCRIPTION_OPTION).getAsString(),
                t.getOption(InfMessages.PHOTO_OPTION).getAsAttachment().getUrl(),
                t.getOption(InfMessages.URL_OPTION).getAsString(),
                usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()));
        t.reply(InfMessages.EDIT_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByDiscordName(data.get(InfMessages.NAME_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda().getGuildById(user.getServersConnect().getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelById(data.get(InfMessages.TELEGRAM_CHANNEL).asText());

        textChannel.sendMessage(data.findValue(InfMessages.NAME_FROM_TELEGRAM).asText() + InfMessages.EDIT_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "edit";
    }


}
