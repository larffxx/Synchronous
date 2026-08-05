package com.larffxx.synchronousdiscord.domain.context;

import net.dv8tion.jda.api.EmbedBuilder;


import java.util.List;

public record EmbedContext(EmbedBuilder embedBuilder,
                           List<String> raw) {}
