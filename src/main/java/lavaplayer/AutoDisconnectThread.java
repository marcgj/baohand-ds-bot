package lavaplayer;

import net.dv8tion.jda.api.managers.AudioManager;

import javax.xml.stream.FactoryConfigurationError;
import java.util.concurrent.BlockingQueue;

public class AutoDisconnectThread extends Thread{

    private final long TIMEOUT;
    private final BlockingQueue<?> queue;

    private final AudioManager manager;


    public AutoDisconnectThread(long timeout, BlockingQueue<?> queue, AudioManager manager) {
        this.TIMEOUT = timeout;
        this.queue = queue;
        this.manager = manager;

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
