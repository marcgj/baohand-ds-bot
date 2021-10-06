package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlaye;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer audioPlaye) {
        this.audioPlaye = audioPlaye;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    @Override
    public boolean canProvide() {
        return this.audioPlaye.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return this.buffer.flip();
    }
}
