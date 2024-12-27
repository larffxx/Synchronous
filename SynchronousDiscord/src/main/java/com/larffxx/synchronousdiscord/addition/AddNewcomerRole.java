package com.larffxx.synchronousdiscord.addition;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class AddNewcomerRole implements Addable<GuildMemberJoinEvent>{
    private Guild guild;
    private Role role;
    private TextChannel newcomerChannel;


    @Override
    public void add(GuildMemberJoinEvent e) {
        Member member = e.getMember();
        guild = e.getGuild();
        role = guild.getRolesByName("Newcomer", true).get(0);

        createRoleIfNotExist();

        guild.addRoleToMember(member.getUser(), role).queue();
    }

    private void createRoleIfNotExist() {
        guild.createRole()
                .setName("Newcomer")
                .setColor(new Color(0x4074C4)).queue();

        addPermissionToViewNewcomersChannel();
        denyPermissionToViewAnotherChannels();
    }

    private void addPermissionToViewNewcomersChannel() {
        createTextChannelIfNotExists();
        newcomerChannel = guild.getTextChannelsByName("Newcomers", true).get(0);
        newcomerChannel.upsertPermissionOverride(role).grant(Permission.VIEW_CHANNEL).queue();
    }

    private void denyPermissionToViewAnotherChannels() {
        for (TextChannel otherChannel : guild.getTextChannels()){
            if(!otherChannel.equals(newcomerChannel)){
                otherChannel.upsertPermissionOverride(role).deny(Permission.VIEW_CHANNEL).queue();
            }
        }
    }
    private void createTextChannelIfNotExists() {
        guild.createTextChannel("Newcomers").queue();
    }
}
