package lavaplayer;

import Utils.ThumbnailExtractor;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;


    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));

                if (musicManager.scheduler.getQueue().size() == 0) {
                    builder.setTitle("Reproduint:");
                } else {
                    builder.setTitle("Posant a la cua:");
                }

                builder.setDescription(String.format("[`%s`](%s)", audioTrack.getInfo().title, audioTrack.getInfo().uri));
                builder.setThumbnail(ThumbnailExtractor.getThumbnailUrl(audioTrack.getInfo().uri));
                channel.sendMessage(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (audioPlaylist.isSearchResult()) {
                    trackLoaded(tracks.get(0));
                    return;
                }


                for (final AudioTrack track : tracks) {
                    musicManager.scheduler.queue(track);
                }

                if (musicManager.scheduler.getQueue().size() == 0) {
                    channel.sendMessage("Reproduint la playlist: `" + audioPlaylist.getName() + "`").queue();
                } else {
                    channel.sendMessage("Posant a la cua la playlist: `" + audioPlaylist.getName() + "`").queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }
}
