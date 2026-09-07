package com.larffxx.synchronousdiscord.infrastructure.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * Add Newcomer Role class.
 */
@Component
public class AddNewcomerRole implements Addable<GuildMemberJoinEvent>{
    /**
     * The guild.
     */
    private Guild guild;
    /**
     * The role.
     */
    private Role role;
    /**
     * The newcomer channel.
     */
    private TextChannel newcomerChannel;

    /**
     * Adds item.
     * @param e the guild member join event.
     */
    @Override
    public void add(GuildMemberJoinEvent e) {
        Member member = e.getMember();
        guild = e.getGuild();

        if (guild.getRolesByName("Newcomer", true).isEmpty()) {
            createRoleIfNotExist();
        }
        role = guild.getRolesByName("Newcomer", true).get(0);

        guild.addRoleToMember(member.getUser(), role).queue();
    }

    /**
     * Creates role if not exist.
     */
    private void createRoleIfNotExist() {
        guild.createRole()
                .setName("Newcomer")
                .setColor(new Color(0x4074C4)).queue();

        addPermissionToViewNewcomersChannel();
        denyPermissionToViewAnotherChannels();
    }

    /**
     * Adds permission to view newcomers channel.
     */
    private void addPermissionToViewNewcomersChannel() {
        createTextChannelIfNotExists();
        newcomerChannel = guild.getTextChannelsByName("Newcomers", true).get(0);
        newcomerChannel.upsertPermissionOverride(role).grant(Permission.VIEW_CHANNEL).queue();
    }

    /**
     * Denies permission to view another channels.
     */
    private void denyPermissionToViewAnotherChannels() {
        for (TextChannel otherChannel : guild.getTextChannels()){
            if(!otherChannel.equals(newcomerChannel)){
                otherChannel.upsertPermissionOverride(role).deny(Permission.VIEW_CHANNEL).queue();
            }
        }
    }
    /**
     * Creates text channel if not exists.
     */
    private void createTextChannelIfNotExists() {
        if (guild.getTextChannelsByName("Newcomers", true).isEmpty()) {
            guild.createTextChannel("Newcomers").queue();
        }
    }
}
