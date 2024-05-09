package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.model.Profile;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.repo.UsersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CreateProfileCommand extends Command {
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final UsersConnectRepository usersConnectRepository;

    public CreateProfileCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO, UsersConnectRepository usersConnectRepository) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
        this.usersConnectRepository = usersConnectRepository;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        if (!profileDAO.existsByUsersConnect(usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()))) {
            Profile profile = new Profile(t.getOption("description").getAsString(), t.getOption("photo").getAsAttachment().getUrl(),t.getOption("url").getAsString(),
                    usersConnectRepository.getReferenceById(usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()).getId()));
            profileDAO.saveModel(profile);
            t.reply("Profile created").queue();
        } else {
            t.reply("You have created profile already, can edit with /edit").queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        getEventReceiver().getJda()
                .getGuildById(usersConnectDAO.getByTelegramName(data.findValue("name").asText()).getServersConnect().getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0)
                .sendMessage(data.findValue("name").asText() + "profile created").queue();
    }

    @Override
    public String getCommand() {
        return "create";
    }
}
