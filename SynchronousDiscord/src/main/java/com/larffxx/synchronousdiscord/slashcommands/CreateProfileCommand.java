package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.InfMessages;
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


    public CreateProfileCommand(EventReceiver eventReceiver, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO, UsersConnectRepository usersConnectRepository) {
        super(eventReceiver);
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.usersConnectRepository = usersConnectRepository;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        if (!profileDAO.existsByUsersConnect(usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()))) {
            Profile profile = new Profile(t.getOption(InfMessages.DESCRIPTION_OPTION).getAsString(),
                    t.getOption(InfMessages.PHOTO_OPTION).getAsAttachment().getUrl(),
                    t.getOption(InfMessages.URL_OPTION).getAsString(),
                    usersConnectRepository.getReferenceById(usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()).getId()));
            profileDAO.saveModel(profile);
            t.reply(InfMessages.CREATE_PROFILE_SUCCESS_MESSAGE).queue();
        } else {
            t.reply(InfMessages.CREATE_PROFILE_UNSUCCESSFUL_MESSAGE).queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        UsersConnect user = usersConnectDAO.getByDiscordName(data.get(InfMessages.NAME_FROM_TELEGRAM).asText());
        Guild guild = getEventReceiver().getJda().getGuildById(user.getServersConnect().getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelById(data.get(InfMessages.TELEGRAM_CHANNEL).asText());

        textChannel.sendMessage(data.findValue("name").asText() + InfMessages.CREATE_PROFILE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "create";
    }

}
