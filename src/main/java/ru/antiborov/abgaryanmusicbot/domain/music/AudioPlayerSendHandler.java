package ru.antiborov.abgaryanmusicbot.domain.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Wrapper class for {@link AudioPlayer} which makes it to behave as JDA {@link AudioSendHandler}
 */
public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer, int byteBufferSize) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(byteBufferSize);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(this.buffer);
    }

    /**
     * If this method returns true JDA will attempt to retrieve audio data from this handler by calling
     * {@link #provide20MsAudio()}. The return value is checked each time JDA attempts send audio, so if
     * the developer wanted to start and stop sending audio it could be done by changing the value returned
     * by this method at runtime.
     *
     * @return If true, JDA will attempt to retrieve audio data from {@link #provide20MsAudio()}
     */
    @Override
    public boolean canProvide() {
        return audioPlayer.provide(frame);
    }

    /**
     * If {@link #canProvide()} returns true JDA will call this method in an attempt to retrieve audio data from the
     * handler. This method need to provide 20 Milliseconds of audio data as a <b>array-backed</b> {@link ByteBuffer}.
     * Use either {@link ByteBuffer#allocate(int)} or {@link ByteBuffer#wrap(byte[])}.
     * <p>
     * Considering this system needs to be low-latency / high-speed, it is recommended that the loading of audio data
     * be done before hand or in parallel and not loaded from disk when this method is called by JDA. Attempting to load
     * all audio data from disk when this method is called will most likely cause issues due to IO blocking this thread.
     * <p>
     * The provided audio data needs to be in the format: 48KHz 16bit stereo signed BigEndian PCM.
     * <br>Defined by: {@link AudioSendHandler#INPUT_FORMAT AudioSendHandler.INPUT_FORMAT}.
     * <br>If {@link #isOpus()} is set to return true, then it should be in pre-encoded Opus format instead.
     *
     * @return Should return a {@link ByteBuffer} containing 20 Milliseconds of audio.
     * @see #isOpus()
     * @see #canProvide()
     * @see ByteBuffer#allocate(int)
     * @see ByteBuffer#wrap(byte[])
     */
    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        ((Buffer) buffer).flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
