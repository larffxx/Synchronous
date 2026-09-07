package com.larffxx.synchronousdiscord.infrastructure;

import com.larffxx.synchronousdiscord.domain.context.MemberContext;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

/**
 * Member Context Resolver class.
 */
@Component
public class MemberContextResolver {
    /**
     * Resolves member context.
     * @param member the member.
     * @return the resulting member context.
     */
    public MemberContext resolveMemberContext(Member member) {
        return new MemberContext(member.getId(), member.getUser().getName(), member.getNickname(), member.getGuild().getId());
    }
}
