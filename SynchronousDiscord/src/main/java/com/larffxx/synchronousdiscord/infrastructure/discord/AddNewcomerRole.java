package com.larffxx.synchronousdiscord.infrastructure.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.stereotype.Component;

import java.awt.Color;

/**
 * Adds the newcomer role and configures the newcomer channel.
 */
@Component
public class AddNewcomerRole implements Addable<GuildMemberJoinEvent> {
    @Override
    public void add(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        Role existingRole = guild.getRolesByName("Newcomer", true).stream().findFirst().orElse(null);
        if (existingRole != null) {
            configureRole(guild, existingRole, member);
            return;
        }

        guild.createRole()
                .setName("Newcomer")
                .setColor(new Color(0x4074C4))
                .queue(role -> configureRole(guild, role, member));
    }

    private void configureRole(Guild guild, Role role, Member member) {
        TextChannel existingChannel = guild.getTextChannelsByName("Newcomers", true).stream()
                .findFirst().orElse(null);
        if (existingChannel != null) {
            configurePermissions(guild, existingChannel, role, member);
            return;
        }

        guild.createTextChannel("Newcomers")
                .queue(channel -> configurePermissions(guild, channel, role, member));
    }

    private void configurePermissions(Guild guild, TextChannel newcomerChannel, Role role, Member member) {
        newcomerChannel.upsertPermissionOverride(role)
                .grant(Permission.VIEW_CHANNEL)
                .queue();
        for (TextChannel otherChannel : guild.getTextChannels()) {
            if (!otherChannel.equals(newcomerChannel)) {
                otherChannel.upsertPermissionOverride(role)
                        .deny(Permission.VIEW_CHANNEL)
                        .queue();
            }
        }
        guild.addRoleToMember(member, role).queue();
    }
}
