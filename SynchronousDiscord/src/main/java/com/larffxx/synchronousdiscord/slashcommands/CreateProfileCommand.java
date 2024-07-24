package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
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
public class CreateProfileCommand extends Command {
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final UsersConnectRepository usersConnectRepository;

    private final String SUCCESS_MESSAGE = "Profile was created successfully";
    private final String UNSUCCESSFUL_MESSAGE = "You have created a profile already, can edit with /edit";
    private final String DESCRIPTION_OPTION = "description";
    private final String PHOTO_OPTION = "photo";
    private final String URL_OPTION = "url";
    private final String NAME_FROM_TELEGRAM = "name";
    private final String TELEGRAM_TEXT_CHANNEL = "telegram";

    public CreateProfileCommand(EventReceiver eventReceiver, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO, UsersConnectRepository usersConnectRepository) {
        super(eventReceiver);
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.usersConnectRepository = usersConnectRepository;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        if (!profileDAO.existsByUsersConnect(usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()))) {
            Profile profile = new Profile(t.getOption(DESCRIPTION_OPTION).getAsString(),
                    t.getOption(PHOTO_OPTION).getAsAttachment().getUrl(),
                    t.getOption(URL_OPTION).getAsString(),
                    usersConnectRepository.getReferenceById(usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()).getId()));
            profileDAO.saveModel(profile);
            t.reply(SUCCESS_MESSAGE).queue();
        } else {
            t.reply(UNSUCCESSFUL_MESSAGE).queue();
        }
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
        return "create";
    }

}
