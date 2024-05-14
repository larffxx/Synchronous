package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ProfileDAO;
import com.larffxx.synchronousdiscord.dao.UsersConnectDAO;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfileCommand extends Command {
    private final ProfileDAO profileDAO;
    private final UsersConnectDAO usersConnectDAO;
    private final CommandListener commandListener;

    public ProfileCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, CommandListener commandListener, ProfileDAO profileDAO, UsersConnectDAO usersConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.commandListener = commandListener;
        this.profileDAO = profileDAO;
        this.usersConnectDAO = usersConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (profileDAO.existsByUsersConnect(profileDAO.getUsersConnectDAO().getByDiscordName(event.getInteraction().getUser().getName()))) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(event.getUser().getName())
                    .setTitle(event.getUser().getName() + ": profile")
                    .setDescription(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getDescription())
                    .setImage(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getPhotoUrl())
                    .setUrl(profileDAO.getProfile(usersConnectDAO.getByDiscordName(event.getUser().getName()).getDiscordName()).getSocialUrl());
            commandListener.getEmbedSender().send(eb);
            event.reply("Your profile").queue();
        } else {
            event.reply("Create profile with /create command").queue();
        }
    }

    @Override
    public void execute(JsonNode data) {
        if (profileDAO.existsByUsersConnect(profileDAO.getUsersConnectDAO().getByTelegramName(data.findValue("name").asText()))) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(data.findValue("name").asText())
                    .setTitle(data.findValue("name").asText() + ": profile")
                    .setDescription(profileDAO.getProfile(usersConnectDAO.getByTelegramName(data.findValue("name").asText()).getDiscordName()).getDescription())
                    .setImage(profileDAO.getProfile(usersConnectDAO.getByTelegramName(data.findValue("name").asText()).getDiscordName()).getPhotoUrl())
                    .setUrl(profileDAO.getProfile(usersConnectDAO.getByTelegramName(data.findValue("name").asText()).getDiscordName()).getSocialUrl());
            commandListener.getEmbedSender().send(eb);
        } else {
            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(data.findValue("name").asText())
                    .setTitle("Error")
                    .setDescription("Create profile with /create command");
            commandListener.getEmbedSender().send(eb);
        }
    }

    @Override
    public String getCommand() {
        return "profile";
    }
}
