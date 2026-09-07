package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import com.larffxx.synchronousdiscord.service.CoolnessService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Gives coolness points to a selected guild member.
 */
@Service
public class CoolCommand implements Command {
    private final CoolnessService coolnessService;

    /**
     * Creates a cool command.
     * @param coolnessService service tracking coolness ratings
     */
    public CoolCommand(CoolnessService coolnessService) {
        this.coolnessService = coolnessService;
    }

    /**
     * Adds coolness to the selected member.
     * @param event Discord slash command interaction event
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member target = event.getOption(CommandConstants.COOL_USER_OPTION) != null
                ? event.getOption(CommandConstants.COOL_USER_OPTION).getAsMember()
                : null;
        if (target == null || target.getUser().isBot()) {
            event.getHook().editOriginal(CommandConstants.COOLNESS_SELECT_MEMBER).queue();
            return;
        }
        int amount = event.getOption(CommandConstants.COOL_AMOUNT_OPTION) != null
                ? event.getOption(CommandConstants.COOL_AMOUNT_OPTION).getAsInt()
                : 1;
        if (amount < 1 || amount > 10) {
            event.getHook().editOriginal(CommandConstants.COOLNESS_INVALID_AMOUNT).queue();
            return;
        }
        try {
            int total = coolnessService.addCoolness(target, amount);
            event.getHook().editOriginal(formatTotal(target.getEffectiveName(), total)).queue();
        } catch (CommandException e) {
            event.getHook().editOriginal(e.getMessage()).queue();
        }
    }

    /**
     * Adds coolness to a member named in Telegram options.
     * @param telegramCommandContext Telegram command context
     */
    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        List<String> options = telegramCommandContext.options();
        TextChannel textChannel = telegramCommandContext.textChannel();
        if (options.isEmpty()) {
            throw new CommandException(CommandConstants.COOLNESS_USAGE);
        }
        Guild guild = telegramCommandContext.guild();
        Member target = guild.getMembersByName(options.get(0), true).stream().findFirst().orElse(null);
        if (target == null) {
            textChannel.sendMessage(CommandConstants.COOLNESS_UNREGISTERED).queue();
            return;
        }
        int amount = 1;
        if (options.size() > 1) {
            try {
                amount = Integer.parseInt(options.get(1));
            } catch (NumberFormatException e) {
                textChannel.sendMessage(CommandConstants.COOLNESS_INVALID_AMOUNT).queue();
                return;
            }
        }
        if (amount < 1 || amount > 10) {
            textChannel.sendMessage(CommandConstants.COOLNESS_INVALID_AMOUNT).queue();
            return;
        }
        try {
            int total = coolnessService.addCoolness(target, amount);
            textChannel.sendMessage(formatTotal(target.getEffectiveName(), total)).queue();
        } catch (CommandException e) {
            textChannel.sendMessage(e.getMessage()).queue();
        }
    }

    /**
     * Returns the cool command name.
     * @return cool command name
     */
    @Override
    public String getCommand() {
        return "cool";
    }

    /**
     * Formats a coolness total with stars.
     * @param name display name
     * @param total rating value
     * @return formatted message
     */
    private String formatTotal(String name, int total) {
        String stars = coolnessService.starsSuffix(total);
        return String.format(CommandConstants.COOLNESS_ADDED, name, total) + (stars.isEmpty() ? "" : " " + stars);
    }
}
