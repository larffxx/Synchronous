package com.larffxx.synchronousdiscord.domain.record;

public record MemberContext(
        String id,
        String effectiveName,
        String guildName,
        String guildId) {
}
