package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
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

    private final String SUCCESS_MESSAGE = "Profile was edited";
    private final String DESCRIPTION_OPTION = "description";
    private final String PHOTO_OPTION = "photo";
    private final String URL_OPTION = "url";
    private final String NAME_FROM_TELEGRAM = "name";
    private final String TELEGRAM_TEXT_CHANNEL = "telegram";

    public EditProfileCommand(EventReceiver eventReceiver, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO) {
        super(eventReceiver);
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        profileDAO.updateProfile(t.getOption(DESCRIPTION_OPTION).getAsString(),
                t.getOption(PHOTO_OPTION).getAsAttachment().getUrl(),
                t.getOption(URL_OPTION).getAsString(),
                usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()));
        t.reply(SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByDiscordName(data.get(NAME_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda().getGuildById(user.getServersConnect().getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelById(data.get(TELEGRAM_TEXT_CHANNEL).asText());

        textChannel.sendMessage(data.findValue("name").asText() + SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "edit";
    }


}
