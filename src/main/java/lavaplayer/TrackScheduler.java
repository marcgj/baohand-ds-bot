package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    public int queued = 0;


    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext){
            nextTrack();
            queued--;
        }
    }

    public void nextTrack() {
        this.player.startTrack(this.queue.poll(), false);
    }

    public void queue(AudioTrack track){
        if (!this.player.startTrack(track, true)){
            this.queue.offer(track);
            queued++;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int i  = 0;


        for (AudioTrack t : queue){
            int converted = (int) t.getDuration()/1000;

            int hours = converted / 3600;
            int remainder = converted - hours * 3600;
            int mins = remainder / 60;
            remainder = remainder - mins * 60;
            int secs = remainder;

            String time = String.format("%s:%s:%s", hours, mins, secs);

            String line = String.format("**%s.**  `%s` - [%s]\n", i+1, t.getInfo().title, time);
            builder.append(line);
            i++;
        }
        return builder.toString();
    }
}