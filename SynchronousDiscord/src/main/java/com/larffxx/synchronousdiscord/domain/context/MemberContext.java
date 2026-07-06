package com.larffxx.synchronousdiscord.domain.context;

public record MemberContext(
        String id,
        String effectiveName,
        String guildName,
        String guildId) {
}
