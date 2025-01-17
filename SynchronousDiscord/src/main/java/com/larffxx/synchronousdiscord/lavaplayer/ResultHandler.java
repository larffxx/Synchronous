package com.larffxx.synchronousdiscord.lavaplayer;

import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.github.topi314.lavasrc.yandexmusic.YandexMusicSourceManager;
import com.larffxx.synchronousdiscord.listeners.LavaplayerSecretsHolder;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.EmbedSender;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final LavaplayerSecretsHolder lavaplayerSecretsHolder;
    private final EmbedSender embedSender;

    public ResultHandler(EventReceiver eventReceiver, LavaplayerSecretsHolder lavaplayerSecretsHolder, EmbedSender embedSender) {
        this.lavaplayerSecretsHolder = lavaplayerSecretsHolder;
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.registerSourceManager(new SpotifySourceManager(null, lavaplayerSecretsHolder.getSpotifyClientId(), lavaplayerSecretsHolder.getSpotifyClientSecret(), "US", audioPlayerManager));
        audioPlayerManager.registerSourceManager(new YandexMusicSourceManager(lavaplayerSecretsHolder.getYandexAccessToken()));
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true, true, true));

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
        this.eventReceiver = eventReceiver;
        this.embedSender = embedSender;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        this.getMusicManager(eventReceiver.getTextChannel().getGuild()).getScheduler().queue(track);
        EmbedBuilder musicEB = new EmbedBuilder();
        musicEB.setDescription("A new music has been added to queue.");
        musicEB.addField("Music", track.getInfo().title, false);
        musicEB.addField("Author", track.getInfo().author, false);
        embedSender.send(musicEB);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        final List<AudioTrack> tracks = playlist.getTracks();
        EmbedBuilder playlistEb = new EmbedBuilder();
        playlistEb.addField("Playlist", playlist.getName(), false);
        for (AudioTrack track : tracks) {
            getMusicManager(eventReceiver.getTextChannel().getGuild()).getScheduler().queue(track);
        }
        embedSender.send(playlistEb);
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException exception) {
        EmbedBuilder eb = new EmbedBuilder().setDescription("Smth went wrong");
        embedSender.send(eb);
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
