package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import com.larffxx.synchronousdiscord.service.CoolnessService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Shows the coolness rating of a member as text.
 */
@Service
public class CoolnessCommand implements Command {
    private final CoolnessService coolnessService;

    /**
     * Creates a coolness command.
     * @param coolnessService service tracking coolness ratings
     */
    public CoolnessCommand(CoolnessService coolnessService) {
        this.coolnessService = coolnessService;
    }

    /**
     * Shows the coolness of the selected member or the invoker.
     * @param event Discord slash command interaction event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member target = event.getOption(CommandConstants.COOL_USER_OPTION) != null
                ? event.getOption(CommandConstants.COOL_USER_OPTION).getAsMember()
                : event.getMember();
        if (target == null) {
            event.getHook().editOriginal(CommandConstants.COOLNESS_SELECT_MEMBER).queue();
            return;
        }
        try {
            int total = coolnessService.getCoolness(target.getId());
            event.getHook().editOriginal(formatTotal(target.getEffectiveName(), total)).queue();
        } catch (CommandException e) {
            event.getHook().editOriginal(e.getMessage()).queue();
        }
    }

    /**
     * Shows the coolness of a named member or the Telegram sender.
     * @param telegramCommandContext Telegram command context
     */
    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        List<String> options = telegramCommandContext.options();
        TextChannel textChannel = telegramCommandContext.textChannel();
        try {
            int total;
            String name;
            if (!options.isEmpty()) {
                Member target = telegramCommandContext.guild().getMembersByName(options.get(0), true)
                        .stream().findFirst().orElse(null);
                if (target == null) {
                    textChannel.sendMessage(CommandConstants.COOLNESS_UNREGISTERED).queue();
                    return;
                }
                total = coolnessService.getCoolness(target.getId());
                name = target.getEffectiveName();
            } else {
                total = coolnessService.getCoolnessByTelegramName(telegramCommandContext.telegramUsername());
                name = telegramCommandContext.telegramUsername();
            }
            textChannel.sendMessage(formatTotal(name, total)).queue();
        } catch (CommandException e) {
            textChannel.sendMessage(e.getMessage()).queue();
        }
    }

    /**
     * Returns the coolness command name.
     * @return coolness command name
     */
    @Override
    public String getCommand() {
        return "coolness";
    }

    /**
     * Formats a coolness total with stars.
     * @param name display name
     * @param total rating value
     * @return formatted message
     */
    private String formatTotal(String name, int total) {
        String stars = coolnessService.starsSuffix(total);
        return String.format(CommandConstants.COOLNESS_SHOW, name, total) + (stars.isEmpty() ? "" : " " + stars);
    }
}
