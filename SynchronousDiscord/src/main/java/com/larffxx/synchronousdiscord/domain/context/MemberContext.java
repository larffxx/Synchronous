package com.larffxx.synchronousdiscord.domain.context;

/**
 * Member Context record.
 * @param id the id.
 * @param username the username.
 * @param guildName the guild name.
 * @param guildId the guild id.
 */
public record MemberContext(
        String id,
        String username,
        String guildName,
        String guildId) {
}
