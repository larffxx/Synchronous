package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EditProfileCommand extends Command{
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;

    public EditProfileCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        profileDAO.updateProfile(t.getOption("description").getAsString(),
                t.getOption("photo").getAsAttachment().getUrl(),
                t.getOption("url").getAsString(),
                usersConnectDAO.getByDiscordName(t.getInteraction().getUser().getName()));
        t.reply("Profile was edited").queue();
    }

    @Override
    public void execute(JsonNode data) {
        getEventReceiver().getJda()
                .getGuildById(usersConnectDAO.getByTelegramName(data.findValue("name").asText()).getServersConnect().getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0)
                .sendMessage(data.findValue("name").asText() + " profile was edited").queue();
    }

    @Override
    public String getCommand() {
        return "edit";
    }
}
