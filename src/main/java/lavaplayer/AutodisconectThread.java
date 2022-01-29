package lavaplayer;

import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.BlockingQueue;

public class AutodisconectThread extends Thread{

    private final long TIMEOUT;
    private final BlockingQueue<?> queue;

    private final AudioManager manager;


    public AutodisconectThread(long timeout, BlockingQueue<?> queue, AudioManager manager) {
        this.TIMEOUT = timeout;
        this.queue = queue;
        this.manager = manager;

    }

    @Override
    public void run() {
        for (int i = 0; i < TIMEOUT; i++){
            var track = queue.peek();
            if(track != null) return;
            try{
                sleep(1000);
            } catch (Exception e){
                return;
            }
        }
        manager.closeAudioConnection();
    }
}
