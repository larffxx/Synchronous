package com.larffxx.synchronousdiscord.lavaplayer;

import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class ResultHandler implements AudioLoadResultHandler {
    private final EventReceiver eventReceiver;
    private AudioPlayerManager audioPlayerManager;
    private Map<Long, GuildMusicManager> musicManagers;
    private final CommandListener commandListener;


    public ResultHandler(EventReceiver eventReceiver, CommandListener commandListener) {
        this.commandListener = commandListener;
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.registerSourceManager(new SpotifySourceManager(null, commandListener.getClientId(), commandListener.getClientSecret(), "US", audioPlayerManager));

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        this.getMusicManager(eventReceiver.getTextChannel().getGuild()).getScheduler().queue(track);
        EmbedBuilder musicEB = new EmbedBuilder();
        musicEB.setDescription("A new music has been added to queue.");
        musicEB.addField("Music", track.getInfo().title, false);
        musicEB.addField("Author", track.getInfo().author, false);
        commandListener.getEmbedSender().send(musicEB);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        final List<AudioTrack> tracks = playlist.getTracks();
        EmbedBuilder playlistEb = new EmbedBuilder();
        playlistEb.addField("Playlist", playlist.getTracks().get(0).getInfo().uri, false);
        for (AudioTrack track : tracks) {
            getMusicManager(eventReceiver.getTextChannel().getGuild()).getScheduler().queue(track);
        }
        commandListener.getEmbedSender().send(playlistEb);
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException exception) {
        EmbedBuilder eb = new EmbedBuilder().setDescription("Smth went wrong");
        commandListener.getEmbedSender().send(eb);
        exception.printStackTrace();
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }
}
