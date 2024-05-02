package com.larffxx.synchronousdiscord.lavaplayer;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class PlayerManager {
    private final ResultHandler resultHandler;

    public PlayerManager(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }


    public void loadAndPlay(TextChannel textChannel, String trackUrl) {
        resultHandler.getMusicManager(textChannel.getGuild());
        resultHandler.getAudioPlayerManager().loadItem(trackUrl, resultHandler);
    }
}
