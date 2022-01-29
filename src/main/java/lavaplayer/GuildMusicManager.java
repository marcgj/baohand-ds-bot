package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.managers.AudioManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;

    private final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager audioPlayerManager, AudioManager audioManager) {
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer, audioManager);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}
