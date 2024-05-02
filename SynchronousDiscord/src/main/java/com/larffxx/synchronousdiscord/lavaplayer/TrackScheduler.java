package com.larffxx.synchronousdiscord.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
    private AudioPlayer audioPlayer;
    private LinkedList<AudioTrack> queue;
    private Queue<AudioTrack> tracks = new LinkedBlockingQueue<>();
    private boolean repeat;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedList<>();
    }

    public void queue(AudioTrack track) {
        if (!audioPlayer.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        if (repeat) {
            queue(audioPlayer.getPlayingTrack());
        }
        AudioTrack nextTrack = queue.poll();
        if(nextTrack == null){
            audioPlayer.stopTrack();
        }else {
            audioPlayer.startTrack(nextTrack.makeClone(), false);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer audioPlayer, AudioTrack audioTrack, AudioTrackEndReason audioTrackEndReason) {
        if (audioTrackEndReason.mayStartNext) {
            if (isRepeat()) {
                queue(audioTrack.makeClone());
            }
            nextTrack();
        }
    }

    public void stopTrack() {
        audioPlayer.stopTrack();
        queue.clear();
    }

}
