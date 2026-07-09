package com.larffxx.synchronousdiscord.domain.context;

public record MemberContext(
        String id,
        String username,
        String guildName,
        String guildId) {
}
