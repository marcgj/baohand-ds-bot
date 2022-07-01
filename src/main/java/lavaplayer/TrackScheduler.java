package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.PrimitiveIterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public static final Dotenv dotenv = Dotenv.load();

    private static final long TIMEOUT = Long.parseLong(dotenv.get("VOICE_TIMEOUT"));

    private AudioTrack actualTrack;

    private final AudioManager manager;

    private boolean loop = false;

    public void setLoop(boolean state){
        loop = state;
    }

    public boolean toggleLoop(){
        loop = !loop;
        return loop;
    }


    public TrackScheduler(AudioPlayer player, AudioManager manager) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.manager = manager;
    }

    private volatile AutoDisconnectThread disconnectThread;

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack audioTrack, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            //var disconnectThread = new AutoDisconnectThread(TIMEOUT, queue, manager);
            //disconnectThread.start();
            nextTrack();
        }
    }

    public void nextTrack() {
        AudioTrack track;
        if (loop)
            track = actualTrack.makeClone();
        else
            track = this.queue.poll();

        assert track != null;
        System.out.println("Next track: " + track.getInfo().title);
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
        System.out.println("New audio track: " + track.getInfo().title);
        if (!this.player.startTrack(track, true))
            this.queue.offer(track);
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void setActualTrack(AudioTrack actualTrack) {
        this.actualTrack = actualTrack;
    }

    private static class AutoDisconnectThread extends Thread{

        private final long TIMEOUT;
        private final BlockingQueue<?> queue;


        public AutoDisconnectThread(long timeout, BlockingQueue<?> queue, AudioManager manager) {
            this.TIMEOUT = timeout;
            this.queue = queue;

        }

        private boolean running = true;

        @Override
        public void run() {
            while (running){
                for (int i = 0; i < TIMEOUT; i++){
                    var track = queue.peek();
                    if(track != null) return;
                    try{
                        sleep(1000);
                    } catch (Exception e){
                        return;
                    }
                }
                running = false;
            }
        }

        public synchronized void stopThread(){
            running = false;
        }
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
