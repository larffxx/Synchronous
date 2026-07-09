package com.larffxx.synchronousdiscord.infrastructure;

import com.larffxx.synchronousdiscord.domain.context.MemberContext;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;


@Component
public class MemberContextResolver {
    public MemberContext resolveMemberContext(Member member) {
        return new MemberContext(member.getId(), member.getUser().getName(), member.getNickname(), member.getGuild().getId());
    }
}
