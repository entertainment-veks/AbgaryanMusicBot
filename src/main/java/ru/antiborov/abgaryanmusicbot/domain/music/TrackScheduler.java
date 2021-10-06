package ru.antiborov.abgaryanmusicbot.domain.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

@Log
@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private final BlockingQueue<AudioTrack> queue;
    private boolean repeat;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        // If something is playing - the track will be added to the queue
        if (!audioPlayer.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public AudioTrack nextTrack() {
        AudioTrack track = queue.poll();
        audioPlayer.playTrack(track);
        return track;
    }

    // Audio Event Handling

    /**
     * @param player Audio player
     */
    @Override
    public void onPlayerPause(AudioPlayer player) {
        log.fine("AudioPlayer has paused playing");
    }

    /**
     * @param player Audio player
     */
    @Override
    public void onPlayerResume(AudioPlayer player) {
        log.fine("AudioPlayer has resumed playing");
    }

    /**
     * @param player Audio player
     * @param track  Audio track that started
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.fine(
                "AudioPlayer has started playing "
                        + track.getInfo().uri);
    }

    /**
     * @param player    Audio player
     * @param track     Audio track that ended
     * @param endReason The reason why the track stopped playing
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (repeat)
            queue.offer(track.makeClone());

        if (endReason.mayStartNext)
            nextTrack();

        log.fine(
                "AudioPlayer track has ended with code "
                        + endReason
                        + " - "
                        + track.getInfo().uri);
    }

    /**
     * @param player    Audio player
     * @param track     Audio track where the exception occurred
     * @param exception The exception that occurred
     */
    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        switch (exception.severity) {
            case COMMON:
                // fallthrough
            case SUSPICIOUS:
                log.warning(
                        "AudioPlayer has encountered an exception while playing "
                                + exception
                                + " - "
                                + track.getInfo().uri);
                break;
            case FAULT:
                log.severe(
                        "AudioPlayer has encountered a fatal exception while playing "
                                + exception
                                + " - "
                                + track.getInfo().uri);
                break;
        }
    }

    /**
     * @param player      Audio player
     * @param track       Audio track where the exception occurred
     * @param thresholdMs The wait threshold that was exceeded for this event to trigger
     */
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        log.warning(
                "AudioPlayer has exceeded Threshold of "
                        + thresholdMs
                        + " millis on track "
                        + track.getInfo().uri);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
        log.severe(
                "AudioPlayer has exceeded Threshold of "
                        + thresholdMs
                        + " millis on track "
                        + track.getInfo().uri
                        + " stacktrace: "
                        + Arrays.toString(stackTrace));
    }

    public Stream<AudioTrack> getQueueAsStream() {
        return queue.stream();
    }
}
