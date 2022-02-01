package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public static final Dotenv dotenv = Dotenv.load();

    private static final long TIMEOUT = Long.parseLong(dotenv.get("VOICE_TIMEOUT"));

    private final AudioManager manager;

    public TrackScheduler(AudioPlayer player, AudioManager manager) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.manager = manager;
    }

    private AutoDisconnectThread disconnectThread;

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack audioTrack, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            disconnectThread = new AutoDisconnectThread(TIMEOUT, queue, manager);
            disconnectThread.start();
            nextTrack();
        }
    }

    public void nextTrack() {
        var track = this.queue.poll();
        System.out.println(track);
        this.player.startTrack(track, false);
    }

    public void nextTrack(int position) {
        for (int i = 0; i < position - 1; i++) {
            this.queue.poll();
        }
        this.player.startTrack(this.queue.poll(), false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void queue(AudioTrack track) {
        disconnectThread.interrupt();
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    /*

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int i = 0;


        for (AudioTrack t : queue) {
            int converted = (int) t.getDuration() / 1000;

            int hours = converted / 3600;
            int remainder = converted - hours * 3600;
            int mins = remainder / 60;
            remainder = remainder - mins * 60;
            int secs = remainder;

            String time = String.format("%s:%s:%s", hours, mins, secs);

            String line = String.format("**%s.**  `%s` - [%s]\n", i + 1, t.getInfo().title, time);
            builder.append(line);
            i++;
        }
        return builder.toString();
    }

     */
}
