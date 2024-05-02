package com.larffxx.synchronousdiscord.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuildMusicManager {

    private final AudioPlayer audioPlayer;
    private final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;


    public GuildMusicManager(AudioPlayerManager manager) {
        audioPlayer = manager.createPlayer();
        scheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(scheduler);
        sendHandler = new AudioPlayerSendHandler(audioPlayer);
    }

    public AudioPlayerSendHandler getSendHandler(){
        return sendHandler;
    }

}
